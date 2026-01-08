package hk.gov.cmc.utils.common;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoggingErrorListener implements ErrorListener {

  protected static Log logger = LogFactory.getLog(LoggingErrorListener.class);

  public LoggingErrorListener() {
  }

  public void warning(TransformerException exception) {

    logger.warn("Warn caught at LoggingErrorListener", exception);
  }

  public void error(TransformerException exception)
      throws TransformerException {

    logger.error("Error caught at LoggingErrorListener", exception);
    throw exception;

  }

  public void fatalError(TransformerException exception)
      throws TransformerException {

    logger.error("Fatal Error caught at LoggingErrorListener", exception);
    throw exception;

  }
}
