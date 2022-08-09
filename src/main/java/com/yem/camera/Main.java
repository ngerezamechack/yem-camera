/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package com.yem.camera;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author NGEREZA
 */
public class Main extends Application {
    
     @Override
    public void start(Stage primaryStage) {
        YemCamera camera = new YemCamera();
        camera.openCamera(null);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
