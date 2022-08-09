/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.yem.camera;

import javafx.scene.image.Image;

/**
 *
 * @author NGEREZA
 */
public interface CamObserve {
    public void onCapture(Image image);
    
    //
    public void onCancel();
}
