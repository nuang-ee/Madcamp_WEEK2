const claimerModel = require('../../models/claimer');
const userModel = require('../../models/user');
const ObjectId = require('mongodb').ObjectId


exports.getClaimer = (req, res) => {
  const {
    uid
  } = req.body
  userModel.find({
    uid
  }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send(e);
    } else {
      res.json(user.claimer.filter(e => e.sent === false && e.received === false))
    }
  });
}

exports.addClaim = (req, res) => {
  const {
    uid,
    claimer,
    amount,
    account,
    name,
    date
  } = req.body;
  userModel.find({
    uid
  }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send(e);
    } else {
      const newClaimer = new claimerModel();
      newClaimer.claimer = claimer;
      newClaimer.amount = amount;
      newClaimer.account = account;
      newClaimer.name = name;
      newClaimer.date = date;
      newClaimer.markModified("claimer");
      newClaimer.markModified('amount');
      newClaimer.markModified('account');
      newClaimer.markModified('name');
      newClaimer.markModified('date');
      user.claimer.push(newClaimer);
      user.save(err => {
        if (err) {
          console.error(err);
          res.json({
            result: 0
          });
        } else {
          res.json({
            _id: newClaimer._id,
            result: 1
          });
        }
      });
    }
  })
}

exports.receiveAmount = (req, res) => {
  const {
    uid,
    _id
  } = req.body
  userModel.find({
    uid
  }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send(e);
    } else {
      user.claimee.map(
        e => {
          if (JSON.stringify(e._id) === JSON.stringify(ObjectId(_id))) {
            if (e.sent) {
              e.received = true
            } else {
              res.json({
                result: 0
              })
            }
          }
        }
      )
      user.save(err => {
        if (err) {
          console.error(err);
          res.json({
            result: 0
          });
        } else {
          res.json({
            result: 1
          });
        }
      })
    }
  });
}