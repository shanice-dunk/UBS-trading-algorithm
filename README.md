# MyAlgoLogic

### Objective
The objective of the MyAlgoLogic was to write a simple trading algo that adds and cancels orders. 
The stretch exercise was to write an algo that can make money buy buying shares when the order book is cheaper, and selling them when the order book is more expensive.

### MoSCoW
#### Must Haves:
1. Add child orders logic
2. Cancel child orders logic
3. Back testing
4. Unit testing

#### Should Haves:
1. Logic that makes money by buying shares when order book is cheaper
2. Logic that makes money by selling shares when order book is expensive

#### Could Haves:
1. Market trend logic (upward, downward, volatile etc.)
2. Average prices logic
3. Buy and sell threshold logic

#### Won't Have This Time:
1. Overly complicated logic that I cannot explain!

### How to Run Tests 
1. Please be aware this project requires Java 17 or higher

#### Note 
This project is configured for Java 17. If you have a later version installed, it will compile and run successfully, but you may see warnings in the log like this, which you can safely ignore:

```sh
[WARNING] system modules path not set in conjunction with -source 17
```

Firstly, run the Maven `install` task to make sure the binary encoders and decoders are installed and available for use. You can use the provided Maven wrapper or an installed instance of Maven, either in the command line or from the IDE integration.

Run the following command from the project root: 

- `./mvnw clean install`

Once you've done this, you can compile or test specific projects using the `--projects` flag, e.g.:

Run the following test command from the project root to test the MyAlgoLogic:

- `./mvnw test --projects algo-exercise/getting-started`

For other commands, see below:
- Clean all projects: `./mvnw clean`
- Test all `algo-exercise` projects: `./mvnw test --projects algo-exercise`
- Compile the `getting-started` project only: `./mvnw compile --projects algo-exercise/getting-started`

# UI Exercise
