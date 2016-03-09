package br.edu.ufal;

import br.edu.ufal.controller.CADController;
import org.jooby.Jooby;

/**
 * @author jooby generator
 */
public class App extends Jooby {

  {
    assets("/bower_components/**");
    assets("/js/**");
    assets("/css/**");
    assets("/pages/**");
    assets("/resources/**");


    assets("/", "index.html");



    use(CADController.class);
  }


  public static void main(final String[] args) throws Exception {
    new App().start(args);
  }

}
