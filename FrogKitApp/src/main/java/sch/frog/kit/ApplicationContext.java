package sch.frog.kit;

import sch.frog.kit.common.CustomViewControl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ApplicationContext {

    private final ArrayList<CustomViewControl> views = new ArrayList<>();

    void addView(CustomViewControl view){
        views.add(view);
    }

    void addViews(Collection<CustomViewControl> views){
        this.views.addAll(views);
    }

    public List<CustomViewControl> getViews(){
        return Collections.unmodifiableList(views);
    }

}
