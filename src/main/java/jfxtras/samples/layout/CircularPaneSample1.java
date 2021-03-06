package jfxtras.samples.layout;

import java.math.BigDecimal;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.BigDecimalField;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.layout.CircularPane;
import jfxtras.scene.layout.GridPane;
import jfxtras.scene.layout.HBox;
import jfxtras.scene.layout.VBox;


public class CircularPaneSample1 extends JFXtrasSampleBase
{
    public CircularPaneSample1() {
    	circularPane = new CircularPane();
    	for (int i = 0; i < 1; i++) {
    		circularPane.add(new Rectangle(30, 30));
    	}

    	// on animate, also animate in again
    	circularPane.setOnAnimateOutFinished( (eventHandler) -> {
    		circularPane.setVisible(false);
        	Platform.runLater(() -> {
        		sleep(1000);
        		circularPane.setVisible(true);
        		circularPane.animateIn();
        	});
    	});
    }
    final CircularPane circularPane;

    @Override
    public String getSampleName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getSampleDescription() {
        return "Basic CircularPane usage";
    }

    @Override
    public Node getPanel(Stage stage) {
		this.stage = stage;
		
        VBox root = new VBox(20);
        root.setPadding(new Insets(30, 30, 30, 30));

        root.getChildren().addAll(circularPane);

        return root;
    }
	private Stage stage;
	
    @Override
    public Node getControlPanel() {
        // the result
        GridPane lGridPane = new GridPane();
        lGridPane.setVgap(2.0);
        lGridPane.setHgap(2.0);

        // setup the grid so all the labels will not grow, but the rest will
        ColumnConstraints lColumnConstraintsAlwaysGrow = new ColumnConstraints();
        lColumnConstraintsAlwaysGrow.setHgrow(Priority.ALWAYS);
        ColumnConstraints lColumnConstraintsNeverGrow = new ColumnConstraints();
        lColumnConstraintsNeverGrow.setHgrow(Priority.NEVER);
        lGridPane.getColumnConstraints().addAll(lColumnConstraintsNeverGrow, lColumnConstraintsAlwaysGrow);
        int lRowIdx = 0;

        // showDebug
        {
            Label lLabel = new Label("Show debug");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            CheckBox lCheckBox = new CheckBox();
            lCheckBox.setTooltip(new Tooltip("Show layout debug hints"));
            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
            lCheckBox.selectedProperty().addListener( (invalidationEvent) -> {
            	circularPane.setShowDebug( lCheckBox.isSelected() ? Color.GREEN : null );
            });
        }
        lRowIdx++;
        
        // Shape
        {
            lGridPane.add(new Label("Shape"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            lGridPane.add(shapeChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            shapeChoiceBox.getSelectionModel().select(0);
            shapeChoiceBox.getSelectionModel().selectedItemProperty().addListener((invalidationEvent) -> {
            	reconstructPane();
            });
        }
        lRowIdx++;

        // number of shopes
        {
            lGridPane.add(new Label("Number of shapes"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            lGridPane.add(amountBigDecimalField, new GridPane.C().row(lRowIdx).col(1));
            amountBigDecimalField.numberProperty().addListener( (observableValue) -> {
            	reconstructPane();
            });
        }
        lRowIdx++;

        // Children are circular
        {
            Label lLabel = new Label("Children are circular");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            CheckBox lCheckBox = new CheckBox();
            lCheckBox.setTooltip(new Tooltip("Enable the optimized rendering for when all children are circular (or smaller)"));
            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
            circularPane.childrenAreCircularProperty().bind(lCheckBox.selectedProperty());
        }
        lRowIdx++;

        // Animation
        {
            lGridPane.add(new Label("Animation"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            animationChoiceBox.getSelectionModel().select(0);
            
            // run the animation
            Button lButton = new Button("Animate");
            lButton.setOnAction( (eventHandler) -> {
           		circularPane.setAnimationInterpolation( convertAnimationInterPolation(animationChoiceBox) );
               	circularPane.animateOut();
            });
            lGridPane.add(new HBox(3).add(animationChoiceBox).add(lButton), new GridPane.C().row(lRowIdx).col(1));
        }
        lRowIdx++;

        // Gap
        {
            Label lLabel = new Label("Gap");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            BigDecimalField lBigDecimalField = new BigDecimalField(BigDecimal.valueOf(0));
            lBigDecimalField.setTooltip(new Tooltip("Gap between nodes"));
            lGridPane.add(lBigDecimalField, new GridPane.C().row(lRowIdx).col(1));
            lBigDecimalField.numberProperty().addListener( (observableValue) -> {
            	circularPane.setGap(lBigDecimalField.getNumber().doubleValue());
            });
        }
        lRowIdx++;

        // Start angle
        {
            Label lLabel = new Label("Start angle");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            BigDecimalField lBigDecimalField = new BigDecimalField(BigDecimal.valueOf(0));
            lBigDecimalField.setTooltip(new Tooltip("Start angle for first node (in degrees)"));
            lGridPane.add(lBigDecimalField, new GridPane.C().row(lRowIdx).col(1));
            lBigDecimalField.numberProperty().addListener( (observableValue) -> {
            	circularPane.setStartAngle(lBigDecimalField.getNumber().doubleValue());
            });
        }
        lRowIdx++;

        // Arc
        {
            Label lLabel = new Label("Arc");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            BigDecimalField lBigDecimalField = new BigDecimalField(BigDecimal.valueOf(360));
            lBigDecimalField.setTooltip(new Tooltip("Render on a partial arc (in degrees)"));
            lGridPane.add(lBigDecimalField, new GridPane.C().row(lRowIdx).col(1));
            lBigDecimalField.numberProperty().addListener( (observableValue) -> {
            	circularPane.setArc(lBigDecimalField.getNumber().doubleValue());
            });
        }
        lRowIdx++;

        // Clip excess whitespace
        {
            Label lLabel = new Label("Clip excess whitespace");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            CheckBox lCheckBox = new CheckBox();
            lCheckBox.setTooltip(new Tooltip("If rendered in an arc, clip away anyt excess whitespace"));
            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
            circularPane.clipAwayExcessWhitespaceProperty().bind(lCheckBox.selectedProperty());
        }
        lRowIdx++;

		// done
    	reconstructPane();
        return lGridPane;
    }
    private ChoiceBox<String> shapeChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(Rectangle.class.getSimpleName(), Circle.class.getSimpleName()));
    private BigDecimalField amountBigDecimalField = new BigDecimalField(BigDecimal.valueOf(12));
    static public enum Animations {OverTheArc, OverTheArcWithFade, FromOrigin, FromOriginWithFadeRotate, SpiralOut, Appear, None};
    private ChoiceBox<String> animationChoiceBox = animationChoiceBox();
    static public ChoiceBox<String> animationChoiceBox() {
    	return new ChoiceBox<>(FXCollections.observableArrayList(Animations.OverTheArc.toString(), 
    			Animations.OverTheArcWithFade.toString(), 
    			Animations.FromOrigin.toString(), 
    			Animations.FromOriginWithFadeRotate.toString(), 
    			Animations.Appear.toString(), 
    			Animations.SpiralOut.toString(),
    			Animations.None.toString()));
    }
    static public CircularPane.AnimationInterpolation convertAnimationInterPolation(ChoiceBox<String> animationChoiceBox) {
    	String s = animationChoiceBox.getSelectionModel().getSelectedItem();
    	if (Animations.OverTheArc.toString().equals(s)) {
    		return CircularPane::animateOverTheArc;
    	}
    	else if (Animations.OverTheArcWithFade.toString().equals(s)) {
    		return CircularPane::animateOverTheArcWithFade;
    	}
    	else if (Animations.FromOrigin.toString().equals(s)) {
    		return CircularPane::animateFromTheOrigin;
    	}
    	else if (Animations.FromOriginWithFadeRotate.toString().equals(s)) {
    		return CircularPane::animateFromTheOriginWithFadeRotate;
    	}
    	else if (Animations.SpiralOut.toString().equals(s)) {
    		return CircularPane::animateSpiralOut;
    	}
    	else if (Animations.Appear.toString().equals(s)) {
    		return CircularPane::animateAppear;
    	}
    	return null;
    }
     
    private void reconstructPane() {
    	circularPane.getChildren().clear();
    	for (int i = 0; i < amountBigDecimalField.getNumber().intValue(); i++) {
    		String lShapeName = shapeChoiceBox.getSelectionModel().getSelectedItem();    		
    		if (Circle.class.getSimpleName().equals(lShapeName)) {
        		circularPane.add(new Circle(20));
    		}
    		if (Rectangle.class.getSimpleName().equals(lShapeName)) {
    			circularPane.add(new Rectangle(30, 30));
    		}
    	}
    }
    
    private void sleep(int ms) {
    	try {
			Thread.sleep(ms);
		} 
    	catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    @Override
    public String getJavaDocURL() {
        return "http://jfxtras.org/doc/8.0labs/" + CircularPane.class.getName().replace(".", "/") + ".html";
		//return "http://jfxtras.org/doc/8.0/jfxtras-common/" + CalendarPicker.class.getName().replace(".", "/") + ".html";
    }
}