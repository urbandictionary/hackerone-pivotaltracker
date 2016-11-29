# Escalate HackerOne Reports to Pivotal Tracker

This Heroku app lets you click "Escalate" on a HackerOne Report and add it as a bug to Pivotal Tracker.

## Step 1

Deploy your own copy to Heroku:

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/urbandictionary/hackerone-pivotaltracker)

## Step 2

Email support@hackerone.com:

```
Hello,

I'd like to add a custom escalation integration with these URLs:

https://YOUR-HEROKU-APP-NAME.herokuapp.com/create?id=[report id]&url=[report url]&title=[title]&details=[details]
https://YOUR-HEROKU-APP-NAME.herokuapp.com/view?id=[id]

Thank you!
```

Wait for hackerone to confirm that the integration was added.

## Step 3

On a HackerOne Report, choose Change State > Triaged, then click Escalate

## Step 4

After the Pivotal Tracker story is created, paste the Pivotal Tracker story ID into the "Reference ID" input box on the HackerOne Report. That is necessary for the "View" link.

---

# Development

## Run tests

`lein midje :autotest`

## Set up development

cat >profiles.clj

```
{:dev {:env {:dev                       "true"
             :pivotaltracker-api-key    "aaaaaaa"
             :pivotaltracker-project-id "11111111"}}}
```

## Run locally

`lein run`, then open http://localhost:8080/create?id=183837&url=https%3A%2F%2Fhackerone.com%2Freports%2F1111&title=Race+condition&details=foobar