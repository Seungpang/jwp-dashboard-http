package org.apache.coyote.http.response;

import org.apache.coyote.http.ContentType;
import org.apache.coyote.http.HttpHeader;
import org.apache.coyote.http.HttpVersion;
import org.apache.coyote.http.session.Session;

public class HttpResponse {

    private final HttpVersion version;
    private final HttpStatus httpStatus;
    private final String responseBody;
    private final HttpHeader httpHeader;

    public HttpResponse(final HttpStatus httpStatus, final ContentType contentType, final String responseBody) {
        this(HttpVersion.HTTP11, httpStatus, responseBody, HttpHeader.init(contentType, responseBody));
    }

    private HttpResponse(final HttpVersion version, final HttpStatus httpStatus, final String responseBody,
                         final HttpHeader httpHeader) {
        this.version = version;
        this.httpStatus = httpStatus;
        this.responseBody = responseBody;
        this.httpHeader = httpHeader;
    }

    public static HttpResponse createResponseByRedirectUrl(final HttpStatus httpStatus, final ContentType contentType,
                                                           final String responseBody, final String redirectUrl) {
        return new HttpResponse(HttpVersion.HTTP11, httpStatus, responseBody,
                HttpHeader.init(contentType, responseBody, redirectUrl));
    }

    public String convertTemplate() {
        return String.join("\r\n",
                version.getVersion() + " " + httpStatus.getHttpStatusMessage(),
                printHeader() + "\n",
                this.responseBody);
    }

    public String printHeader() {
        return httpHeader.print();
    }

    public void addJSessionId(final Session session) {
        httpHeader.addJSessionId(session);
    }
}
