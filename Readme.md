# Postman Collection Jenkins Plugin

## How to Test

`mvn hpi:run -Djetty.port=9001` 
then go to browser: localhost:9001/jenkins 

## How To Build

`mvn package` 

## How To Use

Add your API key to Global Configuration

Create a Build Step and select Postman Collections

Create filename where collection JSON will be stored to in workspace

## Dependencies

None but there's no point in having a Postman Collection without having Newman
