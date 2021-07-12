const socket = require("socket.io");
const express = require("express");
const { socketListener } = require('./controller')

const PORT = process.env.PORT || 3000;
const app = express();

//set up server
const server = app.listen(PORT, () => {
     console.log(`Listening on port ${PORT}...`);
});


//set up socketIO
const io = socket(server);
io.on("connect", socketListener);



//test
let cnt = 0;
app.get('/', (req, res) => {
     const test = {
          a: 1,
          b: cnt++,
          test: "test app draw"
     }
     console.log('sending...')
     setTimeout(() => {
          console.log('done!')
          res.send(test)
     }, 1000)
});