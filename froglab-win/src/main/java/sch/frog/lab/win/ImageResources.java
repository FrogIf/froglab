package sch.frog.lab.win;

import javafx.scene.image.Image;

import java.util.Objects;

public class ImageResources {

    public static Image appIcon = new Image(Objects.requireNonNull(ImageResources.class.getResourceAsStream("/logo.png")));

}
