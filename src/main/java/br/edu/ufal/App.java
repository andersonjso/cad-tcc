package br.edu.ufal;

import br.edu.ufal.controller.CADController;
import org.jooby.Jooby;

/**
 * @author jooby generator
 */
public class App extends Jooby {

  {
    get("/", () -> "Hello World!");

    use(CADController.class);
  }


  public static void main(final String[] args) throws Exception {
    new App().start(args);
  }

}
