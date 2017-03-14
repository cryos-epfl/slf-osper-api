package ch.epfl.cryos.osper.api.util;

import ch.slf.pro.common.util.exception.SlfProRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * Created by kryvych on 14/03/17.
 */
public class ClientErrorHandler implements ResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw SlfProRuntimeException.builder(response.getStatusText(), "")
                    .status(HttpStatus.NOT_FOUND).build();
        }

        throw SlfProRuntimeException.builder(response.getStatusText(), "")
                .status(response.getStatusCode()).build();
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        if ((response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR)
                || (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR)) {
            return true;
        }
        return false;
    }
}
