
# create-credential-store.md

# Create and Configure an Elytron Credential Store in WildFly 32

## Overview

This document explains how to create an **Elytron Credential Store** (`credential-store.cs`) in **WildFly 32**, add secrets to it, register it with WildFly, and use it instead of storing plaintext passwords in `standalone.xml`.

---

# Prerequisites

* WildFly 32 installed
* `WILDFLY_HOME` configured
* WildFly server stopped (recommended when creating the credential store)
* Access to `jboss-cli.sh` and `elytron-tool.sh`

Example:

```text
WILDFLY_HOME=/opt/wildfly
```

---

# Step 1 – Create the Credential Store

Navigate to the WildFly directory:

```bash
cd $WILDFLY_HOME
```

Create the credential store:

```bash
bin/elytron-tool.sh credential-store \
    --create \
    --location=standalone/configuration/credential-store.cs
```

The tool will prompt for:

```
Credential Store Password:
Confirm Credential Store Password:
```

Choose a strong password.

> **Important**
>
> This password protects the credential store itself.
> It is **NOT** the database password.

---

# Step 2 – Add Secrets

Example: Store a database password.

```bash
bin/elytron-tool.sh credential-store \
    --add=dbPassword \
    --location=standalone/configuration/credential-store.cs
```

The tool will ask:

```
Credential Store Password:
Secret to store:
```

Example:

```
Alias:
dbPassword

Secret:
MyDatabasePassword123!
```

You can create additional aliases:

```text
dbPassword
smtpPassword
ldapPassword
apiKey
```

---

# Step 3 – Verify Stored Aliases

List all aliases:

```bash
bin/elytron-tool.sh credential-store \
    --aliases \
    --location=standalone/configuration/credential-store.cs
```

Example output:

```text
Aliases:
--------
dbPassword
smtpPassword
```

---

# Step 4 – Register the Credential Store

Start WildFly.

Connect using the CLI:

```bash
bin/jboss-cli.sh --connect
```

Register the credential store:

```cli
/subsystem=elytron/credential-store=myCredentialStore:add(
    path=credential-store.cs,
    relative-to=jboss.server.config.dir,
    credential-reference={clear-text="CredentialStorePassword"},
    create=false
)
```

Parameters:

| Parameter              | Description                              |
| ---------------------- | ---------------------------------------- |
| `myCredentialStore`    | Name of the Elytron credential store     |
| `path`                 | Credential store file                    |
| `relative-to`          | WildFly configuration directory          |
| `credential-reference` | Password protecting the credential store |
| `create=false`         | Use an existing credential store         |

Verify:

```cli
/subsystem=elytron/credential-store=myCredentialStore:read-resource
```

---

# Step 5 – Use the Credential Store in a Datasource

Instead of:

```xml
<security>
    <user-name>appuser</user-name>
    <password>MyDatabasePassword123!</password>
</security>
```

Configure:

```cli
/subsystem=datasources/data-source=ExampleDS:write-attribute(
    name=credential-reference,
    value={
        store="myCredentialStore",
        alias="dbPassword"
    }
)
```

Remove the old password:

```cli
/subsystem=datasources/data-source=ExampleDS:undefine-attribute(name=password)
```

Result:

```xml
<credential-reference
    store="myCredentialStore"
    alias="dbPassword"/>
```

No plaintext password is stored in `standalone.xml`.

---

# Step 6 – Reload WildFly

```cli
reload
```

---

# Step 7 – Test the Datasource

```cli
/subsystem=datasources/data-source=ExampleDS:test-connection-in-pool
```

Expected result:

```text
{
    "outcome" => "success",
    "result" => [true]
}
```

---

# Updating a Secret

To replace an existing secret:

```bash
bin/elytron-tool.sh credential-store \
    --add=dbPassword \
    --location=standalone/configuration/credential-store.cs
```

Using the same alias updates the stored value.

---

# Removing an Alias

```bash
bin/elytron-tool.sh credential-store \
    --remove=dbPassword \
    --location=standalone/configuration/credential-store.cs
```

---

# Security Best Practices

* Never commit `credential-store.cs` to source control.
* Never commit the credential store password.
* Restrict file permissions:

```bash
chmod 600 standalone/configuration/credential-store.cs
```

* Store the credential store password in a secure secret management system (e.g., HashiCorp Vault, Kubernetes Secret, AWS Secrets Manager, Azure Key Vault) rather than hardcoding it.
* Use separate credential stores for different environments (Development, UAT, Production).
* Rotate stored secrets periodically.

---

# Example Directory Structure

```text
wildfly/
├── bin/
│   ├── jboss-cli.sh
│   └── elytron-tool.sh
│
└── standalone/
    └── configuration/
        ├── standalone.xml
        └── credential-store.cs
```

---

# Summary

1. Create the credential store with `elytron-tool.sh`.
2. Add one or more aliases.
3. Register the credential store in the Elytron subsystem.
4. Reference aliases using `credential-reference`.
5. Remove plaintext passwords from the configuration.
6. Reload WildFly and verify connectivity.
