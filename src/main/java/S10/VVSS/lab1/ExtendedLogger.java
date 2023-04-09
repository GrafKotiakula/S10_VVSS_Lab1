package S10.VVSS.lab1;

import org.slf4j.Logger;

import java.util.Optional;

public class ExtendedLogger {
    private final Logger logger;

    public ExtendedLogger(Logger logger) {
        this.logger = logger;
    }

    private String generateMessage(Throwable ex) {
        return ex.getClass().getSimpleName() +
                Optional.ofNullable(ex.getMessage())
                        .map(msg -> {
                            if(msg.isEmpty()) {
                                return null;
                            } else {
                                return ": " + msg;
                            }
                        }).orElse("");
    }

    public <T extends Throwable> T error(T ex) {
        logger.error(generateMessage(ex));
        return ex;
    }

    public <T extends Throwable> T warn(T ex) {
        logger.warn(generateMessage(ex));
        return ex;
    }

    public <T extends Throwable> T info(T ex) {
        logger.info(generateMessage(ex));
        return ex;
    }

    public <T extends Throwable> T debug(T ex) {
        logger.debug(generateMessage(ex));
        return ex;
    }

    public <T extends Throwable> T trace(T ex) {
        logger.trace(generateMessage(ex));
        return ex;
    }

}
