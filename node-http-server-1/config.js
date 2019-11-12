let data = null
module.exports = function() {
if(!!data) {
    return data;
}
    
data = {};

if(process.env.NODE_ENV == 'production') {
    data = require('./configs/production.json');
    data.NODE_ENV = process.env.NODE_ENV;
} else {
    data = require('./configs/develop.json');
    data.NODE_ENV = 'develop';
}

data.CALL = process.env.CALL || data.CALL;
data.PORT = process.env.PORT || 3000;
return data;

}
