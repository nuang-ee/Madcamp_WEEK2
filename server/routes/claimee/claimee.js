const claimeeModel = require('../../models/claimee');
const userModel = require('../../models/user');
const ObjectId = require('mongodb').ObjectId


exports.getClaimer = (req, res) => {
  // send == true인 경우에는 출력 안해야 함
  res.end();
}

exports.sendAmount = (req, res) => {
  res.end();
}