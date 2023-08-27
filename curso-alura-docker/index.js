const http = require('http');

const port = 3002;


const server = http.createServer((req, res) => {
  res.statusCode = 200;
  res.setHeader('Content-Type', 'text/plain');
  res.end('Hello World');
});


server.listen(process.env.PORT,() => {
  console.log(`Server running at DIOGO SENIOR`);
});