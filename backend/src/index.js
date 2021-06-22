const socket = require("socket.io");
const express = require("express");
const PORT = 3000;

//set up server
const app = express();
const server = app.listen(PORT, () => {
     console.log(`Listening on port ${PORT}...`);
});


const usersMap = {};
const rooms = {};

const log = () => {
     console.log("@@@ LOG: ", JSON.stringify({
          usersMap,
          rooms,
     }, null, 3));
}

var room = "chatRoom";
//set up io
const io = socket(server);
io.on("connect", (socket) => {
     console.log(socket.id, "connected");

     socket.on("REQUEST_JOIN", (room) => {
          if (!rooms[room]) {
               rooms[room] = []
          } else {
               rooms[room].push(socket.id);
          }

          socket.join(room)


          socket.to(room).emit("back", `Room ${room} has new user: ${socket.id}`);

          socket.emit("back", JSON.stringify(rooms));

          usersMap[socket.id] = room

          log()
     });

     socket.on("disconnect", (socket) => {
          // console.log("User left");
     });
});
