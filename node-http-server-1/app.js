const http = require('http');
const os = require('os');
const axios = require('axios');

let config = require("./config")();

let app = http.createServer((req, res) => {
    
    res.writeHead(200, {'Content-Type': 'text/plain'});
    if( req.url === '/call' ){
      const call = config.CALL;
      if( !call ) {
	      res.end(`${os.hostname}: callee is not defined`);
        return;
      }
      
      console.log('calling: ' + call);
      axios.get(call).then(res2 => {
          res.end(os.hostname + " received: " + res2.data);
     }).catch(err => res.end(JSON.stringify(err)));
    }
    else {
    	res.end( `${os.hostname}: hello world responses to ${req.url}\n`);
    }
});

const PORT = config.PORT;

app.listen(PORT, () => {
  console.log(`Server is listening on port ${PORT} in ${config.NODE_ENV}`);
});
