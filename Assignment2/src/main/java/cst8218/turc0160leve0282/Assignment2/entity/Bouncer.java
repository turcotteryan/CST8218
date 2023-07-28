package cst8218.turc0160leve0282.Assignment2.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ryan Turcotte
 * 
 * This is the class that defines the Bouncer entity. It contains the fields and methods needed to properly implement a Bouncer in the application. The @Entity annotation specifies the entity should be represented as a table in the database using the smae name as the class.
 * The @XmlRootElement annotation specifies that this class can be translated to and from xml during serialization.
 */
@Entity
@XmlRootElement
public class Bouncer implements Serializable {
    
    // Width of the in-game x-axis
    public static final int FRAME_WIDTH = 500;
    // Length of the in-game y-axis
    public static final int FRAME_HEIGHT = 500;
    // Rate of acceleration due to gravity
    public static final int GRAVITY_ACCEL = 1;
    // Rate of speed decay on every bounce
    public static final int DECAY_RATE = 5;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Integer xPosition = 0;
    
    private Integer yPosition = 0;
   
    private Integer ySpeed = 0;
    
    public Bouncer() {
        
    }
    
    public Bouncer(Integer xPosition, Integer yPosition, Integer ySpeed) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.ySpeed = ySpeed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getxPosition() {
        return xPosition;
    }

    public void setxPosition(Integer xPosition) {
        this.xPosition = xPosition;
    }

    public Integer getyPosition() {
        return yPosition;
    }

    public void setyPosition(Integer yPosition) {
        this.yPosition = yPosition;
    }

    public Integer getySpeed() {
        return ySpeed;
    }

    public void setySpeed(Integer ySpeed) {
        this.ySpeed = ySpeed;
    }
    
    
    /*
        
        This method updates the values of a bouncer based on the properties of the calling bouncer. For every non-null property of the new bouncer, the old bouncer is updated for that given property.
        If a property of the new bouncer is null, the original data for that property of the old bouncer is retained.
    
        Bouncer bouncer = The old bouncer to be updated
    
    */
    public void updates(Bouncer bouncer) {
        
        if (this.getyPosition() != null) {
            bouncer.setyPosition(this.getyPosition());
        }
        
        if (this.getxPosition() != null) {
            bouncer.setxPosition(this.getxPosition());
        }
        
        if (this.getySpeed() != null) {
            bouncer.setySpeed(this.getySpeed());
        }
    }
    
    
    /** 
     * Updates the properties to simulate the passing of one unit of time. COnatins the logic to keep yPosition within the boundaries of the game in both directions. Applies the effect of gravity
     * for each frame cycle. Calls for the direction reversal and application of decay rate upon a bouncer bouncing off a wall.
     */
    public void advanceOneFrame() {
        
        this.yPosition += this.ySpeed;
        
        if(this.yPosition < 0) {
            this.yPosition = 0;
        } 
        else if(this.yPosition > FRAME_HEIGHT) {
            this.yPosition = FRAME_HEIGHT;
        }
        
        applyGravity();
        
        //if we've bounced, reverse the direction and apply decay
        if(checkForBounce()){
            reverseAndApplyDecay();
        }    
        
    }
    
    /*
        When called, this method applies the effect of gravity on ySpeed. It will simulate accelerating when falling, and decellerating when moving against gravity.
    */
    public void applyGravity() {
        //if bouncer is in motion, accelerate 
        if (ySpeed != 0 && !(yPosition >= FRAME_HEIGHT)) {
            ySpeed += GRAVITY_ACCEL;
        } 
    }
    
    /*
        Returns true or false depending on if yPosition is at or beyond the set boundaries of the game, which signifies a bounce off a wall.
    */
    public boolean checkForBounce() {
        return yPosition <= 0 || yPosition >= FRAME_HEIGHT;
    }
    
    /*
        Called after checking the bouncer has bounced off a wall. If the bouncer is in motion and its bounced, reverse its sign deoending on the new direction (+ when falling, - when rising).
        After reversing, either add or subtract the decay rate to ySpeed in order to reduce it's absolute value no matter the direction.
    */
    public void reverseAndApplyDecay() {
        //ensure bouncer is not stationary
        if (ySpeed != 0) {
            //reverse the direction and apply decay rate based on direction
            ySpeed = -ySpeed; 
            ySpeed += (ySpeed > 0) ? -DECAY_RATE : DECAY_RATE; 
        }
    }
    

    @Override
    public String toString() {
        return "cst8218.turc0160leve0282.Assignment2.entity.Bouncer[ id=" + id + " ]";
    }

    
}
    

