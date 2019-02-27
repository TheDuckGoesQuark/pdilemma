This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

# Client Set-Up

Requires npm to run using these commands. 

Once installed, use `npm install` in the `client` directory to install all relevant dependencies. 
Since create-react-app is fairly large, this may take some time. 

In the project directory, you can then run:

## `npm start`

Runs the app in the development mode.<br>
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br>
You will also see any lint errors in the console.

If the API server is not running, you will probably see the following error in the console:

> Proxy error: Could not proxy request /prosecutor/games from localhost:3000 to http://localhost:8080.
> See https://nodejs.org/api/errors.html#errors_common_system_errors for more information (ECONNREFUSED).

Simply starting the API server will solve this issue. Otherwise, a failure message will be shown throughout the application.

## `npm run build`

Builds the app for production to the `build` folder.<br>
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br>
Your app is ready to be deployed!

# Server Set-Up

Maven is the chosen build tool for the server. 

Once installed, use `mvn clean install` in the `server` directory to install all relevant dependencies, run tests,
 and build the `.jar` file.

In the project directory, you can run:

## `java -jar ${jarfile_name}`

Where `${jarfile_name}` is the name of the `.jar` file produced 
from the `mvn clean install` (Should be `pdilemma-0.0.1-SNAPSHOT.jar`).

Runs the app in the development mode.<br>
Open [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) to view 
the API documentation in the browser.

You will also see all debug level logging messages in the browser.

