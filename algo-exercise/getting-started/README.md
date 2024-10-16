# MyAlgoLogic

### Objective
The objective of the MyAlgoLogic was to write a simple trading algo that adds and cancels orders. 
The stretch exercise was to write an algo that can make money buy buying shares when the order book is cheaper, and selling them when the order book is more expensive.

### How to Access Code
Access the `getting-started` folder within the `algo-exercise` folder. You will find an `src` folder and a `test` folder which contains the logic and tests.
- `MyAlgoLogic` - this contains the Java code for the algo.
- `TestLogic` - this file contains stored code used previously (this code is not part of the tests or the final code)
- `MyAlgoBackTest` - this files contains the backtesting of the algo.
- `MyAlgoTest` - this file contains the unit testing of the algo.
- `AbstractAlgoBackTest` and `AbstractAlgoTest` - these files contain the market data ticks. 

### Algo Logic
`The overall goal of the logic is to follow the price trends (upward or downward) and react by placing buy or sell orders`
- History of best bid and ask prices in a LinkedList 
    - Update list as prices are added (FIFO)
    - Determine market trend (uptrend or downtrend)
- Max price history = 5
- Cancel logic:
    - Cancels buy orders if PriceTrend = Uptrend
    - Cancels sell orders if PriceTrend = Downtrend
- Price Rate of Change (ROC)
    - Measures price change between current price and price from a few periods ago
    - 2% Rate of Change: the algorithm will only act on more significant price movements, ignoring the small fluctuations

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
The aim of the ui exercise was to build a table component that updates with market data provided. 

To access the code, open the folder `ui-front-end > src > components`
- `market-depth` folder - this contains MarketDepthPanel component, MarketDepthFeature and CSS file
- `price-cell` folder - this contains the price cell component 
- `quantity-cell` folder - this contains the quantity cell component 

## install dependencies

Use `ui-front-end` as your working folder (i.e. open that folder in VSCode).
From the root (i.e from within `ui-front-end`), install dependencies ...

```
 npm install
```

or same thing, more succinctly

```
npm i
```

This will download all the dependencies (declared in package.json) and save them in a local `node_modules` folder within `ui-front-end`

## Run the App

There is a "scripts" section in package.json, these are npm scripts that you can run from the command line (in VSCode you can also run them from the "NPM SCRIPTS" section at the bottom of the `Explorer` panel) . These are standard scripts, created when vite first initialised the project

YOU ONLY NEED TO DO THE FIRST OF THE STEPS BELOW, THE REST SHOWN JUST FOR COMPLETENESS

- Run the vite dev server, which will host the app on http://localhost:5173, with full Hot Module Loading (change the source code, app will be updated automatically)

```bash
npm run dev
```

You should see output with a link to tell you that app is running on localhost:5173. You will be able to open http://localhost:5173 in your browser (google Chrome recommended)

For future reference ...

- Build the app, does a 'production' build - output will go in the `dist` folder

```
npm run build
```

- launch the app using production build, will host app on http://localhost:4173

```
npm run preview
```

Note: mostly you will be using `npm run dev` to run the dev server, or as most developers would describe it, you will be running in dev mode.

Finally, if you want to run the linter (eslint) across the codebase - you will see how clean your types are. Note not having your types perfectly defined will NOT stop the app from running. It is possible for the app to run perfectly correctly even when the types are wrong. Remember, types are there to help you in the IDE, they do not affect the running code.

```bash
npm run lint
```
