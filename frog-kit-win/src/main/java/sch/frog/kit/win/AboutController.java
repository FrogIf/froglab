package sch.frog.kit.win;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

public class AboutController {

    @FXML
    private Hyperlink githubLink;

    @FXML
    protected void gotoGithub(){
        FrogKitApplication.getAppHostServices().showDocument(githubLink.getText());
    }

}
