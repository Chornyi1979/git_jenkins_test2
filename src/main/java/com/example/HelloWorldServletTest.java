@RunWith(Arquillian.class)
public class HelloWorldServletTest {

  @Deployment(testable=false)
  public static WebArchive create() {
    return ShrinkWrap.create(WebArchive.class, "hello.war").addClass(HelloWorldServlet.class);
  }

  @Test
  public void should_parse_and_load_configuration_file(@ArquillianResource URL resource) throws IOException {

    URL obj = new URL(resource, "HelloWorld");
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    con.setRequestMethod("GET");

    BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
    }
    in.close();

    assertThat(response.toString(), is("Hello World"));
  }
}
