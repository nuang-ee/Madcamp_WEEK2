const claimerModel = require('../../models/claimer');
const userModel = require('../../models/user');
const ObjectId = require('mongodb').ObjectId


exports.getClaimer = (req, res) => {
  const {
    uid
  } = req.body
  console.log("getClaimer>>",
    req.body)
  userModel.find({
    uid
  }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send({
        message: "Something wrong"
      });
    } else {
      if (user) {
        const tmp = user.claimer.filter(e => e.received === false)
        const done = tmp.map(e => ({
          _id: e._id,
          claimer: e.claimer,
          amount: e.amount,
          account: e.account,
          name: e.name,
          date: e.date,
          sent: e.sent,
          received: e.received
        }))
        res.json(done)
      } else {
        res.json({
          message: 'no user here'
        })
      }
    }
  });
}

exports.addClaimer = (req, res) => {
  const {
    uid,
    claimer,
    amount,
    account,
    name,
    date
  } = req.body;
  console.log(req.body);
  userModel.find({
    uid
  }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send({
        message: "Something wrong"
      });
    } else {
      if (user) {
        let claimer_uid;
        userModel.find({
          name: claimer
        }, (err, [target]) => {
          console.log(target)
          if (err) {
            console.error(err);
            res.status(500).send({
              message: "Something wrong"
            });
          } else {
            if (target) {
              claimer_uid = target.uid
              const newClaimer = new claimerModel();
              newClaimer.claimer_uid = claimer_uid;
              newClaimer.claimer = claimer;
              newClaimer.amount = amount;
              newClaimer.account = account;
              newClaimer.name = name;
              newClaimer.date = date;
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
            } else {
              res.json({
                message: "no target here"
              })
            }
          }
        })
      } else {
        res.json({
          message: "no user here"
        })
      }
    }
  })
}

exports.sentAmount = (req, res) => {
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
      res.status(500).send({
        message: "Something wrong"
      });
    } else {
      if (user) {
        user.claimer.map(
          e => {
            if (JSON.stringify(e._id) === JSON.stringify(ObjectId(_id))) {
              e.sent = true
              receiver = e.claimer_uid
              date = e.date
            }
          }
        )
        user.save(err => {
          if (err) {
            console.error(err);
            res.json({
              result: 0
            });
            return;
          }
        })
      } else {
        res.json({
          message: "no user here"
        })
      }
    }
  })
  userModel.find({
    receiver,
  }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send({
        message: "Something wrong"
      });
    } else {
      if (user) {
        user.claimee.map(e => {
          if (e.date === date) {
            e.sent = true
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
      } else {
        res.json({
          message: "no user here"
        })
      }
    }
  })
}