package com.example.domaci2.arena;

import com.example.domaci2.Main;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Translate;

public class Lamp extends Box {

    public Lamp(double width, double height, double depth, Material material, Translate position){
        super(width, height, depth);

        super.setMaterial(material);

        super.getTransforms().addAll( position );
    }

    public void changeLight ( boolean change ) {
        if (change) {
            super.setMaterial( new PhongMaterial(Color.GRAY));
        } else {
            PhongMaterial material = new PhongMaterial( );
            Image image = new Image( Main.class.getClassLoader().getResourceAsStream("selfIllumination.png"));
            material.setSelfIlluminationMap( image );
            material.setSpecularColor( Color.WHITE );
            material.setSpecularPower( 1 );

            super.setMaterial( material );
        }
    }
}
