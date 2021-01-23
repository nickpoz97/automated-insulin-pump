# automated-insulin-pump
High level implementation of an automated insulin pump written in Java with simulated blood.
## Requirements
* The software needs to monitor blood status through sensors and inform user with values and messages printed on a display.
* It must also lower sugar value if it rises or if it is high.
* Sugar control can be done only if there is enough insulin in the reservoir.
* Blood is abstracted with a mathematical model

## Scenarios
### Starting from a good and stable state, user consumes food with sugar
* Initial Hypothesis
	* sugar is at a good level and is stable, insuline level in reservoir is good
* Normal Flow
	* sugar raises and the controller uses the pump to inject insuline and to lock sugar rising
* Bad Situation
	* sugar level raises at a level above safe bound so it is injected more insulin to lower it. After some minutes sugar level should return to a good state and continues to go down slowly
* Ending
	* Insulin amount is lower than the amount at the beginning
	* Display may inform that insulin is under a certain bound if it is the case

### Starting from a low sugar level, user consumes food with sugar
* Initial Hypothesis
	* sugar is low, so the user consumes something containing sugar
* Normal Flow
	* sugar starts rising and after some minutes sugar level is in a good stare
* Bad Situation 1
	* user hasn' t taken enough sugar and sugar level is stable goes down anyway
* Bad situation 2
	* user has taken too much sugar and level raises very quickly
* Ending
	* Display should indicate to user the status

### Sugar level is dangerously high (hyperglycemia)
* Initial hypothesis
	* sugar level is very high
* Normal flow
	* controller uses pump to inject enough insulin so it stops sugar rising and quickly lowers sugar level
* Bad Situation
	* there is not enough insulin, so user must fill the reservoir with at least the amount indicated
	* once reservoir is refilled, everything should return to normal flow
* Ending
	* Display should indicate that sugar level is going down

## General Design
* This software is designed to be an abstraction of a real time embedded system that runs in polling mode with an infinite loop.
* Since it runs on a general purpose operating sysem, it doesn' t rely on time, but instead it is designed to be executed on a simulated environment where blood is an object that represents an *abstract model* of real blood.
* The controller interacts with sensors interfaces (which may be implemented by a sensor driver) and each of them interacts with the blood model.
* There is a module described by a class called *InputHandler* that simulates eating food with sugar and insulin reservoir filling.
* Insulin and added sugar are expressed in units

## Classes Description
* BloodModel
	* Abstract class that represents an abstraction of the blood trend (regarding sugar)
	* Abstract blood trend is like a linear function **as = bs + t \* ir**
		* **as** is actual sugar level (**as = f(bs, t, ir)**)
		* **bs** is base sugar level (every time sugar or insulin is added it is updated to the last **as**)
		* **t** is time (*1 unit <=> 10 minutes*) since last sugar/insulin addition
		* **ir** is increment rate (raised/lowered by 1 for each unit of sugar/insulin addition)
	* there is an update method that raises **t** by 1 and updates **as** according to actual values
* InteractiveBloodModel
	* implements abstract method *retrieveSugarLevel* of *BloodModel* by updating **as** and returning it
	* this model doesn' t consider sugar consumption by human body 
* Controller
	* class that manages sensors and devices to monitor blood and reservoir status and uses displays to inform user
	* contructor connects components and initilizes blood/insulin status
	* method *play()* is a wrapper for more ordered steps
		1. display actual status (print info on each display)
		1. allows interaction with blood through *InputHandler* class (optional)
		1. updates blood status (*updateSugarMeasurements()*) with *SugarSensor* device
		1. obtains info from the measurements
		1. regulates sugar levels (not if sugar level is low)
		1. obtains remaining insulin with *Pump* device
	* the difference between actual and former sugar level is saved in the attribute *increment* that represents the increment rate **ir**
	* method *addDisplay()* adds a new display
* Display
	* interface for a display device that interprets data given by the *Controller* and prints it
	* it support a queue for additional messages to print and a clock
* InputHandler
	* component that simulates sugar in food eating and insuline reservoir filling
	* sugar addition value indicates an abstraction about sugar in food
		* higher values mean simple sugar (sucrose, glucose, ...) in an high amount
		* highest possible value is an abstraction of max amount of sugar that a person can take, to leave out absurd values like 50k (it is connected with constant *Controller.LOWER_INSULIN_BOUND* since this constant should indicate the minimun amount of insulin to always ensure that sugar level doesn' t rise up or stay high)
* InsulineReservoir
	* abstraction of a limited reservoir
	* exceeding values are truncated by capacity
	* if request exceeds availability, only the available amount is returned (**InsulinAvailabilityException** is thrown)
* Pump
	* interface for a device connected with reservoir, *Controller* and blood that injects insulin or informs about the remaining quantity
* SugarSensor
	*  interface for a device connected with *Controller* and blood that informs about *actualSugarLevel* using the method *retrieveSugarLevel* from *BloodData*
* AutomatedInsulinPump
	* wrapper instances the system and executes an infinite loop

## Quality Assurance
### Test Selection
### Test Coverage