/*
 * Design, Implementation and Support of Common Middleware Components and Reference Applications 
 * 
 * EMessage
 * 
 * Developed by: Gilbert Liao
 * Reviewed by: Kai So
 * Tester: Shaohui Wang
 */
package hk.gov.dpo.mars_cmc.cmc.datatype.message;

// import Java standard package(s) -- BEGIN
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
// import Java standard package(s) -- END

// import third-party package(s) -- BEGIN

// import third-party package(s) -- END


// import internal library package(s) -- BEGIN

// import internal library package(s) -- END


// import module package(s) -- BEGIN

// import module package(s) -- END

/**
 * EMessage class
 */
public class EMessage implements Serializable{

// public constant(s) -- BEGIN


// public constant(s) -- END

// public data member(s) -- BEGIN
// public data member(s) -- END

// public method(s) -- BEGIN


	public String getTemplateID()
	{
        return m_templateID;
    }

    public void setTemplateID(String templateID)
	{
	    m_templateID = templateID;
	}

	public String getTemplateVersion()
	{
        return m_templateVersion;
    }

    public void setTemplateVersion(String templateVersion)
	{
	    m_templateVersion = templateVersion;
	}

// public method(s) -- END

// protected constant(s) -- BEGIN
// protected constant(s) -- END

// protected method(s) -- BEGIN
// protected methods -- END

// private methods -- BEGIN



// private methods -- END


// private data members -- BEGIN
private String m_templateID;
private String m_templateVersion;
// private data members -- END

// private constants -- BEGIN
// private constants -- END



}
