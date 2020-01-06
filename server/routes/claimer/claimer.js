const claimerModel = require('../../models/claimer');
const userModel = require('../../models/user');
const ObjectId = require('mongodb').ObjectId


exports.getClaimee = (req, res) => {
  const { uid } = req.body
  userModel.find({ uid }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send(e);
    } else {
      res.json(user.claimee.filter(e => e.received === false))
    }
  });
}

exports.addClaim = (req, res) => {
  res.end()
}

exports.receiveAmount = (req, res) => {
  res.end()
}