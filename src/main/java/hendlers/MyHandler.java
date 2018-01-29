package hendlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;

public class MyHandler implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {

        URI uri = httpExchange.getRequestURI();
        String requestMethod = httpExchange.getRequestMethod();
        reRoute(uri, requestMethod, httpExchange);
    }

    private void reRoute(URI uri, String requestMethod, HttpExchange httpExchange) {

        Class myHandler = MyHandler.class;
        Method[] methods = myHandler.getDeclaredMethods();

        for(Method method : methods) {

            if(method.isAnnotationPresent(WebRoute.class)){

                String annotationMethod = method.getAnnotation(WebRoute.class).method();
                String annotationPath = method.getAnnotation(WebRoute.class).path();

                if(annotationMethod.equals(requestMethod) && annotationPath.equals(uri.toString())){

                    try{
                        Constructor constructor = MyHandler.class.getDeclaredConstructor();
                        MyHandler handler = (MyHandler) constructor.newInstance();
                        method.invoke(handler, httpExchange);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    private void executeResponse(HttpExchange httpExchange, String responseBody) throws IOException {

        httpExchange.sendResponseHeaders(200, responseBody.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(responseBody.getBytes());
        os.close();
    }

    @WebRoute(path = "/", method = "GET")
    void doMain(HttpExchange httpExchange) throws IOException {

        String response = "<html><body>" +
                "in main page</br>" +
                "<a href=\"/form\">To form</a>" +
                "</body></html>";
        executeResponse(httpExchange, response);
    }

    @WebRoute(path = "/form", method = "GET")
    void doForm(HttpExchange httpExchange) throws IOException {

        String response = "<html><body>" +
                "<form method=\"POST\">\n" +
                "  First name:<br>\n" +
                "  <input type=\"text\" name=\"firstname\">" +
                "  <br>\n" +
                "  Last name:<br>\n" +
                "  <input type=\"text\" name=\"lastname\">" +
                "  <br><br>\n" +
                "  <input type=\"submit\" value=\"Submit\">\n" +
                "</form> " +
                "</body></html>";
        executeResponse(httpExchange, response);
    }

    @WebRoute(path = "/form", method = "POST")
    void doPost(HttpExchange httpExchange) throws IOException {

        String response = "<html><body>" +
                "in post method </br>" +
                "<a href=\"/\">To main page</a>" +
                "</body></html>";
        executeResponse(httpExchange, response);
    }
}
