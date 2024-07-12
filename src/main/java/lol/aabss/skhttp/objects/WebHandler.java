package lol.aabss.skhttp.objects;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class WebHandler implements HttpHandler {
    private final File webFilesDirectory;

    public WebHandler(File webFilesDirectory) {
        this.webFilesDirectory = webFilesDirectory;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestURI = exchange.getRequestURI().getPath();
        if (!requestURI.endsWith("/")) {
            String redirectURI = requestURI + "/";
            exchange.getResponseHeaders().set("Location", redirectURI);
            exchange.sendResponseHeaders(301, -1);
            return;
        }
        String relativePath = requestURI.substring(requestURI.indexOf('/', 1) + 1);
        if (relativePath.isEmpty() || relativePath.equals("/")) {
            relativePath = "index.html";
        }
        File fileRequested = new File(webFilesDirectory, relativePath);
        if (fileRequested.exists() && !fileRequested.isDirectory()) {
            byte[] bytes = new byte[(int) fileRequested.length()];
            try (FileInputStream fis = new FileInputStream(fileRequested)) {
                fis.read(bytes);
            }
            String mimeType = getMimeType(fileRequested.getName());
            exchange.getResponseHeaders().set("Content-Type", mimeType);
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        } else {
            String response = "404 (Not Found)";
            exchange.sendResponseHeaders(404, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // Only common is listed, full list here:
    // https://www.iana.org/assignments/media-types/media-types.xhtml
    private String getMimeType(String fileName) {
        String[] split = fileName.split("\\.");
        String extension = split[split.length-1].toLowerCase();
        return switch (extension) {
            case "3pg" -> "audio/3gpp";
            case "3g2" -> "audio/3gpp2";
            case "7z" -> "application/x-7z-compressed";
            case "aac" -> "audio/aac";
            case "abw" -> "application/x-abiword";
            case "apng" -> "image/apng";
            case "arc" -> "application/x-freearc";
            case "avif" -> "image/avif";
            case "avi" -> "video/x-msvideo";
            case "azw" -> "application/vnd.amazon.ebook";
            case "bin" -> "application/octet-stream";
            case "bmp" -> "image/bmp";
            case "bz" -> "application/x-bzip";
            case "bz2" -> "application/x-bzip2";
            case "cda" -> "application/x-cdf";
            case "csh" -> "application/x-csh";
            case "css" -> "text/css";
            case "csv" -> "text/csv";
            case "doc" -> "application/msword";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "eot" -> "application/vnd.ms-fontobject";
            case "epub" -> "application/epub+zip";
            case "gz" -> "application/gzip";
            case "gif" -> "image/gif";
            case "html", "htm" -> "text/html";
            case "ico" -> "image/vnd.microsoft.icon";
            case "ics" -> "text/calendar";
            case "jar" -> "application/java-archive";
            case "jpeg", "jpg" -> "image/jpeg";
            case "js", "mjs" -> "text/javascript";
            case "json" -> "application/json";
            case "jsonld" -> "application/ld+json";
            case "mid", "midi" -> "audio/midi";
            case "mp3" -> "audio/mpeg";
            case "mp4" -> "video/mp4";
            case "mpeg" -> "video/mpeg";
            case "mpkg" -> "application/vnd.apple.installer+xml";
            case "odp" -> "application/vnd.oasis.opendocument.presentation";
            case "ods" -> "application/vnd.oasis.opendocument.spreadsheet";
            case "odt" -> "application/vnd.oasis.opendocument.text";
            case "oga", "ogg" -> "audio/ogg";
            case "ogv" -> "video/ogg";
            case "ogx" -> "application/ogg";
            case "opus" -> "audio/ogg";
            case "otf" -> "font/otf";
            case "png" -> "image/png";
            case "pdf" -> "application/pdf";
            case "php" -> "application/x-httpd-php";
            case "ppt" -> "application/vnd.ms-powerpoint";
            case "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "rar" -> "application/vnd.rar";
            case "rtf" -> "application/rtf";
            case "sh" -> "application/x-sh";
            case "svg" -> "image/svg+xml";
            case "tar" -> "application/x-tar";
            case "tif", "tiff" -> "image/tiff";
            case "ts" -> "video/mp2t";
            case "ttf" -> "font/ttf";
            case "vsd" -> "application/vnd.visio";
            case "wav" -> "audio/wav";
            case "weba" -> "audio/webm";
            case "webm" -> "video/webm";
            case "webp" -> "image/webp";
            case "woff" -> "font/woff";
            case "woff2" -> "font/woff2";
            case "xhtml" -> "application/xhtml+xml";
            case "xls" -> "application/vnd.ms-excel";
            case "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "xml" -> "application/xml";
            case "xul" -> "application/vnd.mozilla.xul+xml";
            case "zip" -> "application/zip";
            default -> "text/plain";
        };
    }
}
