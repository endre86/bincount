import java.util.ArrayList;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Main
{
	public static final GpioController gpioController = GpioFactory.getInstance();
	public static final GpioPinDigitalOutput GREEN = gpioController.provisionDigitalOutputPin(
		RaspiPin.GPIO_00, "gpio0", PinState.LOW);
	public static final GpioPinDigitalOutput RED = gpioController.provisionDigitalOutputPin(
		RaspiPin.GPIO_01, "gpio1", PinState.LOW);
	public static final GpioPinDigitalOutput BLUE = gpioController.provisionDigitalOutputPin(
		RaspiPin.GPIO_02, "gpio2", PinState.LOW);
	public static final GpioPinDigitalOutput ORANGE = gpioController.provisionDigitalOutputPin(
		RaspiPin.GPIO_03, "gpio3", PinState.LOW);
	public static final GpioPinDigitalOutput WHITE = gpioController.provisionDigitalOutputPin(
		RaspiPin.GPIO_04, "gpio4", PinState.LOW);
	
	public static final  GpioPinDigitalOutput GPIO1 = GREEN;
	public static final  GpioPinDigitalOutput GPIO2 = RED;
	public static final  GpioPinDigitalOutput GPIO3 = BLUE;
	public static final  GpioPinDigitalOutput GPIO4 = ORANGE;
	public static final  GpioPinDigitalOutput GPIO5 = WHITE;

	public static void main(String[] args) {
		Main main = new Main();

		boolean writeOutput = false;
		long countdownTime = 30;
		int numEndBlinks = 10;

		for(int i = 0; i < args.length; i++) {
			switch(args[i].toLowerCase()) {
				case "-o":
					writeOutput = true;
					break;
				case "-t":
					i += 1; // go direct to next to get time..
					if(args.length > i) {
						countdownTime = Long.parseLong(args[i]);
					} 
					else {
						System.out.println("-t must be followed by numerical time to count down");
						System.exit(-1);
					}
					break;
				case "-l":
					i += 1; // go direct to next to get the time value
					if(args.length > i) {
						numEndBlinks = Integer.parseInt(args[i]);	
					}
					else {
						System.out.println("-e must be followed by numerical time to count down");
						System.exit(-1);
					}
					break;
				case "-h":
				case "--help":
					System.out.println("Commands:\n" +
											"\t-t [seconds: int]\n\t\ttime to count down\n" +
											"\t-l [loops: int]\n\t\tHow many times (loops) to blink all lights when countdown is finished.\n" +
											"\t-o\n\t\t output countdown in terminal.");
					System.exit(0);
					break;
			}
		}

		//main.blinkAllLights(2000, 3000, 1000);
		main.turnAllOff();
		main.countdown((countdownTime + 1) * 1000 ,writeOutput); // +1 because of delay. lets face it, this is not a serious application :P
		main.turnAllOff();
		main.blinkAllLights(200, 500, numEndBlinks);
		main.turnAllOff();
		System.exit(0);	
	}

	public void turnAllOff()
	{
		GREEN.setState(PinState.LOW);
		RED.setState(PinState.LOW);
		BLUE.setState(PinState.LOW);
		ORANGE.setState(PinState.LOW);
		WHITE.setState(PinState.LOW);
	}

	public void countdown(long countdownTime, boolean writeOutput) {
		long countdownTo = System.currentTimeMillis() + countdownTime;
		long prevOutput = countdownTo + 1;
		
		while(System.currentTimeMillis() < countdownTo) {
			long secondsLeft = (countdownTo - System.currentTimeMillis()) / 1000;
			
			if(writeOutput && prevOutput != secondsLeft) {
				System.out.println(secondsLeft);
				prevOutput = secondsLeft;
			}

			if(secondsLeft <= 0) {
				break;
			}
				
			showBinary(secondsLeft);
			//try{ Thread.sleep(100); } catch(Exception e){}
		}
		
	}	

	public void showBinary(long number) {
			if(number >= 16) {
				GPIO1.setState(PinState.HIGH);
				number -= 16;
			} else {
				GPIO1.setState(PinState.LOW);
			}

			if(number >= 8) {
				GPIO2.setState(PinState.HIGH);
				number -= 8;
			} else {
				GPIO2.setState(PinState.LOW);
			}

			if(number >= 4) {
				GPIO3.setState(PinState.HIGH);
				number -= 4;
			} else {
				GPIO3.setState(PinState.LOW);
			}
			
			if(number >= 2) {
				GPIO4.setState(PinState.HIGH);
				number -= 2;
			} else {
				GPIO4.setState(PinState.LOW);
			}
			
			if(number >= 1) {
				GPIO5.setState(PinState.HIGH);
				number -= 1;
			} else {
				GPIO5.setState(PinState.LOW);
			}
	}

	public void blinkAllLights(int pulseTime, int pauseTime, int numLoops) {
		for(int loop = 0; loop < numLoops; loop++)
		{
			GREEN.pulse(pulseTime);
			RED.pulse(pulseTime);
			BLUE.pulse(pulseTime);
			ORANGE.pulse(pulseTime);
			WHITE.pulse(pulseTime);
			try{ Thread.sleep(pauseTime); } catch(Exception e) {}
		}
	}
}
