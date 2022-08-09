/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yem.camera;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

/**
 *
 * @author NGEREZA
 */
public class YemCamera extends BorderPane {

    private ScheduledService sc;

    @FXML
    private ImageView imv;

    private ObservableList<CamObserve> camOb = FXCollections.observableArrayList();
    private Alert st = new Alert(Alert.AlertType.NONE, "");
    private DialogPane pancam = new DialogPane();

    //constructeur
    public YemCamera() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/yem/camera/camera.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
            initComponents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //initcomponents
    private void initComponents() {
        pancam.setContent(this);

        st.setDialogPane(pancam);
        //
        st.setResizable(false);
        st.setOnCloseRequest((e) -> {
            e.consume();
        });
    }
    
    //definir la dimension
    private int width = 138;
    private int heigth = 177;

    public void setDimesion(int width, int heigth) {
        this.width = width;
        this.heigth = heigth;
    }

    //ouvir la camera
    private Webcam webcam;

    public void openCamera(String css) {

        List<Webcam> wcs = Webcam.getWebcams();
        ObservableList<Webcam> zebcams = FXCollections.observableArrayList(wcs);
        ChoiceDialog<Webcam> dialog = new ChoiceDialog<>(zebcams.get(0), zebcams);
        dialog.setTitle("Web Cam!");
        dialog.setHeaderText(null);
        dialog.setContentText("Coisir une Caméra");
        dialog.getDialogPane().getStyleClass().add("n2");
        //
        Optional.ofNullable(css).ifPresent(this.getStylesheets()::add);

        Optional<Webcam> choix = dialog.showAndWait();

        choix.ifPresent((Webcam wc) -> {
            webcam = wc;
            if (!webcam.isOpen()) {
                webcam.setViewSize(WebcamResolution.QVGA.getSize());
                webcam.open();
            }
            //
            System.out.println(wc.getName());

            image();
            show();
        });

    }

    private void show() {
        if (st.isShowing()) {
            st.show();
        } else {
            st.show();
        }
    }

    @FXML
    private void capturer() {
        camOb.forEach(c -> {
            c.onCapture(imv.getImage());
        });
        fermer();
    }

    @FXML
    private void annuler(){
        fermer();
    }

    //visualiser la la camera
    private BufferedImage bi;

    //
    private void image() {

        sc = new ScheduledService() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {

                        while (true) {
                            try {
                                if (webcam.isOpen()) {
                                    bi = centerImage(webcam.getImage(), width, heigth);

                                    Platform.runLater(() -> {
                                        imv.setImage(SwingFXUtils.toFXImage(bi, null));
                                    });
                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            if (isCancelled()) {
                                break;
                            }
                        }
                        return null;
                    }
                };
            }
        };
        sc.setPeriod(Duration.seconds(10));
        sc.start();
    }

    private BufferedImage centerImage(BufferedImage b, int w, int h) {
        int dw = (b.getWidth() - w) / 2;
        int dh = (b.getHeight() - h) / 2;
        return b.getSubimage(dw, dh, w, h);
    }

    public void addCamObserve(CamObserve cam) {
        this.camOb.add(cam);
    }

    public void removeCamObserve() {
        this.camOb = FXCollections.observableArrayList();
    }

    //femer la fenetre
    private void fermer() {
        webcam.close();
        sc.cancel();
        st.getDialogPane().getScene().getWindow().hide();
        camOb.forEach(c -> {
            c.onCancel();
        });
    }
}

