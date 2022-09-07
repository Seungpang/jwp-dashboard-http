package nextstep.jwp.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.coyote.http.ContentType;
import org.apache.coyote.http.request.HttpRequest;
import org.apache.coyote.http.response.HttpResponse;
import org.apache.coyote.http.response.HttpStatus;
import nextstep.jwp.util.PathUtils;

public class FileController extends AbstractController {

    public static final String FILE_DELIMITER = "\\.";

    @Override
    protected HttpResponse doGet(final HttpRequest request) throws Exception {
        final String url = request.getUrl();
        final Path path = PathUtils.load(url);

        final String extension = url.split(FILE_DELIMITER)[1];
        final String responseBody = new String(Files.readAllBytes(path));
        final ContentType contentType = ContentType.findContentType(extension);

        return new HttpResponse(HttpStatus.OK, contentType, responseBody);
    }
}
