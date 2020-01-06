const claimeeModel = require('../../models/claimee');
const userModel = require('../../models/user');
const ObjectId = require('mongodb').ObjectId


exports.getClaimee = (req, res) => {
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
      res.json(user.claimee.filter(e => e.received === false && e.sent === false))
    }
  });
}

exports.addClaim = (req, res) => {
  const {
    uid
  } = req.body;
  userModel.find({
    uid
  }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send(e);
    } else {
      // new Claimer list
      const rawClaimeeList = req.body.claimeeList
      const claimeeList = rawClaimeeList.map(e => {
        const claimee = new claimeeModel();
        claimee.claimee = e.claimee;
        claimee.amount = e.amount;
        claimee.name = e.name;
        claimee.date = e.date;
        // sent/received는 default 값이 false
        return claimee
      })
      const claimeeIdList = claimeeList.map(e => e._id)
      user.claimee.push(claimee)
      user.save(err => {
        if (err) {
          console.error(err);
          res.json({
            result: 0
          });
        } else {
          res.json({
            _id: claimeeIdList,
            result: 1
          });
        }
      });
    }
  })
}

exports.sendAmount = (req, res) => {
  const {
    uid,
    _id
  } = req.body;
  let receiver, date;
  userModel.find({
    uid
  }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send(err);
    } else {
      user.claimer.map(
        e => {
          if (JSON.stringify(e._id) === JSON.stringify(ObjectId(_id))) {
            e.sent = true
            receiver = e.claimer
            date = e.date
          }
        }
      )
    }
  })
  userModel.find({
    receiver,
  }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send(err);
    } else {
      user.claimee.map(e => {
        if (e.date === date) {
          e.sent = true
        } else {
          res.json({
            result: 0
          })
        }
      })
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
  })
}