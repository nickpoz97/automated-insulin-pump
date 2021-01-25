# automated-insulin-pump
High level implementation of an automated insulin pump written in Java with simulated blood.
## Requirements
* The software needs to monitor blood status through sensors and inform user with values and messages printed on a display.
* It must also lower sugar value if it rises or if it is high.
* Sugar control can be done only if there is enough insulin in the reservoir.

## General Design
* This software is designed to be an abstraction of a real time embedded system that runs in polling mode with an infinite loop.
* Since it runs on a general purpose operating sysem, it doesn' t rely on time, but instead it is designed to be executed on a simulated environment where blood is an object that represents an *abstract model* of real blood.
* Blood is abstracted with a mathematical model
* The controller interacts with sensors interfaces (which may be implemented by a sensor driver) and each of them interacts with the blood model
* There is a module described by a class called *InputHandler* that simulates eating food with sugar and insulin reservoir filling.
* Insulin and added sugar level are expressed in units
* Sugar status in blood depends on 4 boundaries (each value is expressed in units like sugar level and insulin)

| status          | condition 										 |
| --------------- | -----------------------------------------------  |
| VERY_LOW_SUGAR  | sugar < HYPOGLYCEMIA_BOUND						 |
| LOW_SUGAR       | HYPOGLYCEMIA_BOUND <= sugar < LOWER_SUGAR_BOUND  |
| GOOD            | LOWER_SUGAR_BOUND <= sugar <= UPPER_SUGAR_BOUND  |
| HIGH_SUGAR      | HYPERGLYCEMIA_BOUND >= sugar > UPPER_SUGAR_BOUND |
| VERY_HIGH_SUGAR | sugar > HYPERGLYCEMIA_BOUND						 |

* Insulin reserve is low when the amount in the reservoir is below *LOWER_INSULIN_BOUND*

## Scenarios
These scenarios reflects the behavior of a user aware of his condition

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
	* sugar is below a certain bound (but not in a dangerous state), so the user consumes something containing sugar
* Normal Flow
	* sugar starts rising and after some minutes sugar level is in a good state
* Bad Situation 1
	* user hasn' t taken enough sugar and sugar level doesn' to go up
* Bad situation 2
	* user has taken too much sugar and level raises very quickly
* Ending
	* Display should indicate to user the status

### Starting from a low sugar level, user ignores display
* sugar is below a certain bound (but not in a dangerous state), but the user ignores the information
* Normal flow 2
	* sugar level is low but not in hypoglycemia
* Bad situation
	* sugar level is in hypoglycemia level and is decreasing
* Ending
	* Display should indicate every information

### Sugar level is dangerously high (hyperglycemia)
* Initial hypothesis
	* sugar level is very high (display emphasizes this, so user doesn' t eat anything containing sugar)
* Normal flow
	* controller uses pump to inject enough insulin so it stops sugar rising and quickly lowers sugar level
* Bad Situation
	* there is not enough insulin, so user must fill the reservoir with at least the amount indicated
	* once reservoir is refilled, everything should return to normal flow
* Ending
	* Display should indicate that sugar level is going down

### Sugar level is dangerously low (hypoglycemia)
* Initial hypothesis
	* sugar level is very low (display emphasizes that and user takes some sugar)
* Normal flow
	* user consumes some sugar and sugar level starts to rise
* Bad situation
	* user doesn' t take enough sugar so sugar level doesn' t rise
* Ending
	* Display should indicate sugar level trend

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
Every previous scenario has been tested using an *InputStream* as input and controller status as output check
[link for acceptance tests](https://github.com/nickpoz97/automated-insulin-pump/tree/main/src/test/java/it/univr/acceptanceTesting)
### Test Coverage
* Class: 92.3%
* Method: 97.2%
* Line: 96.5 %
[link for test coverage files](https://github.com/nickpoz97/automated-insulin-pump/tree/main/doc/testReports)
### Diagrams
* [use case diagram](https://github.com/nickpoz97/automated-insulin-pump/blob/main/doc/diagrams/use_case.pdf)
* [sequence diagram polling loop](https://github.com/nickpoz97/automated-insulin-pump/blob/main/doc/diagrams/sequence_diagram_polling_loop.pdf)
* [class diagram](https://github.com/nickpoz97/automated-insulin-pump/blob/main/doc/diagrams/class_diagram.pdf)
* [finite state machine diagram](https://github.com/nickpoz97/automated-insulin-pump/blob/main/doc/diagrams/finite_state_machine.pdf)