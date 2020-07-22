## AWS Educate Account
In this class we make heavy use of Amazon Web Services (AWS), which is Amazon's cloud platform.  AWS provides a "free-tier" account that provides  free, limited access to some AWS services for one year.  Free-tier accounts are available to anyone.  As a university student, you can create something better - an AWS Educate student account.  This is similar to a free-tier account, but it gives you AWS access as long as you are a student (not just for one year).  Be sure to sign up for an AWS Educate student account, not a regular free-tier account.  You might already have an AWS Educate student account, if you had to create one for another class you took previously.  If so, that's great; just use the account you already have.

When signing up for an AWS Educate student account, you will need to provide a credit or debit card.  All of your work for this class can be done using the free services provided by your AWS Educate account, so you should not have to pay any AWS charges.  However, AWS wants a credit or debit card anyway in case you exceed the free limits placed on your AWS Educate account.  If you accidentally accrue AWS charges, that is your responsibility, so be careful.  If you do not have a credit or debit card to give them, or it makes you nervous to give them your card information, you can instead use a prepaid card that can be purchased at many retail stores (prepaid Visa card, prepaid Mastercard, etc.)  NOTE: When signing up for your AWS Educate, they will give you an option of creating a "Starter Account", which can be created without a credit or debit card.  This is NOT what you want; you want a regular AWS Educate account.

Instructions for how to create an AWS Educate account can be found herePreview the document.

**In case you are rejected** (and receive an email that says "Your application cannot be approved at this time... attach a copy of the front and back of your school ID...")

Send an email to:
```aws-cs-educate-form@amazon.com```
Attach a copy of both sides of your school ID. Include the email address of the account that was rejected (should be your @byu.edu address) and, if you know it, the Amazon Account ID (a 10 digit number).

## AWS and Money
When working with AWS, you have a lot of opportunities to (accidentally) spend money. Here are some notes to make sure you don't spend a ton of it.

### DynamoDB 
#### Capacity
**Danger: High**

The Free Tier for AWS allows for a total of 25 Read capacity units and 25 write capacity units. Your Dashboard will show you have many you have used. Anything more than that will be charged. Remember that you'd indexes will also use capacity units.

For Milestone 4, you may need to increase these past the 25. If you do this and then shrink back down to within Free Tier limits within an hour, you may not be charged. We expect the charge for Milestone 4 to be well under $1 if you have a good solution.

This can get expensive real quickly if you don't realize.

 
#### Alarms
**Danger: Low**

Each time you create or modify a table, AWS might recreate Alarms. The Free Tier allows for 8 alarms. You will exceed this if you have more than 2 tables. Remember to clear them!
You will probably get charged a few cents a month if you forget.

 
#### Dax
Do not use this for your project. There is no credit for solutions that have this. There is no free tier. 

 

### Lambda
**Danger: Low**

You get 1 million requests per month and over 1,000 hours of compute time. It will be hard to exceed this. You should consider setting smaller timeouts for your lambdas that you expect to be quick to ensure you don't have large compute times, especially in preparation for testing for Milestone 4 where a bulk operation failure could cause a lot of resources to be used if it cascades. Review your retry policies to ensure that a cycle cannot happen or lots of useless duplicate calls don't happen.

### SQS
**Danger: Low**

You get 1 million requests per month. You are likely going to use under this. Milestone 4 might see a much higher percentage used. If you exceed this for that milestone, it should be fairly cheap still. (A couple cents a month)

## Linking AWS Educate with Regular AWS Account
Technically the two accounts won't be linked but you are able to add the $100 AWS credit to your AWS account just in case you go over your free tier usage.

1. Go to your AWS Educate (awseducate.com (Links to an external site.)) account home page and in the top right hand corner click on "AWS Account"
2. On the "AWS Account" tab select show "Show my AWS Personal Credit"
3. Once you have the promo code sign into your normal AWS Account (aws.amazon.com) and navigate to the "Billing" service. On the left hand side click on the "Credits" tab and then insert the promo code in the correct box to redeem your credit.
