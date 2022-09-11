package nextstep.jwp.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.http.ContentType;
import org.apache.coyote.http.request.HttpRequest;
import org.apache.coyote.http.response.HttpResponse;
import org.apache.coyote.http.response.HttpStatus;
import nextstep.jwp.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final String HTML_EXTENSION = ".html";
    private static final String INDEX_PAGE = "/index.html";

    @Override
    protected HttpResponse doPost(final HttpRequest request) throws Exception {
        final String url = request.getUrl();
        final Path path = PathUtils.load(url + HTML_EXTENSION);
        saveUser(request);
        final String responseBody = new String(Files.readAllBytes(path));
        return HttpResponse.createResponseByRedirectUrl(HttpStatus.FOUND, ContentType.HTML, responseBody, INDEX_PAGE);
    }

    private void saveUser(final HttpRequest request) {
        final String account = request.getParameter("account");
        final String password = request.getParameter("password");
        final String email = request.getParameter("email");

        final Optional<User> findUser = InMemoryUserRepository.findByAccount(account);
        if (findUser.isPresent()) {
            log.info("존재하는 유저입니다.");
            return;
        }
        final User user = new User(account, password, email);
        InMemoryUserRepository.save(user);
        log.info("회원가입 성공 user={}", user);
    }

    @Override
    protected HttpResponse doGet(final HttpRequest request) throws Exception {
        final String url = request.getUrl();
        final Path path = PathUtils.load(url + HTML_EXTENSION);
        final String responseBody = new String(Files.readAllBytes(path));
        return new HttpResponse(HttpStatus.OK, ContentType.HTML, responseBody);
    }
}
