package sch.frog.lab.win;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

public class AboutController {

    @FXML
    private Hyperlink githubLink;

    @FXML
    protected void gotoGithub(){
        FroglabApplication.getAppHostServices().showDocument(githubLink.getText());
    }

}
