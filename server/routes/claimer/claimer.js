const claimerModel = require('../../models/claimer');
const userModel = require('../../models/user');
const ObjectId = require('mongodb').ObjectId


exports.getClaimee = (req, res) => {
  // received == true인 경우 안 보여줘야 함
  res.end()
}

exports.receiveAmount = (req, res) => {
  res.end()
}