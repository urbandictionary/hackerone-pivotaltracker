# Escalate HackerOne Reports to Pivotal Tracker

1. Deploy your own copy to Heroku:

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/urbandictionary/hackerone-pivotaltracker)

2. Email support@hackerone.com:

```
Hello,

I'd like to add a custom escalation integration with these URLs:

https://YOUR-HEROKU-APP-NAME.herokuapp.com/create?id=[report id]&url=[report url]&title=[title]&details=[details]
https://YOUR-HEROKU-APP-NAME.herokuapp.com/view?id=[id]

Thank you!
```

3. On a HackerOne Report, choose Change State > Triaged, then click Escalate

4. After the Pivotal Tracker story is created, paste the Pivotal Tracker story ID into the "Reference ID" input box on the HackerOne Report.