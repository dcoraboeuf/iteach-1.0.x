For a version 1.0, we want:
* a neat API that can interact with other platforms (mobile apps in particular)
* a better integration with the "real life" of a teacher, like the need to import other calendars or to create invoices

## All use cases

... and their link to the pages & API.

### As an administrator...

### As a teacher...

#### I want to register in iTeach:

* I go the home page
* I click on Login to enter
* I select my connection mode: Google, OpenID or Username/Password

... using my Google account:

* I select Google
* I validate the authentication with Google
* I enter the information needed by iTeach
* I receive an email for the confirmation and click on the associated link
* I'm again on the login page where I can validate my login by clicking again on the Google button
* I'm on the planning page

... using my OpenID account:

* I enter my OpenID and click on the Sign in with OpenID button
* I validate the authentication with the OpenID provider
* I enter the information needed by iTeach
* I receive an email for the confirmation and click on the associated link
* I'm again on the login page where I can validate my login by entering again my OpenID and clicking on the Sign in with OpenID button
* I'm on the planning page

... using a custom user name & password:

* I click on Register
* I enter the information needed by iTeach and create a new password
* I receive an email for the confirmation and click on the associated link
* This redirects me to the iTeach login page where I can enter my email and the password
* I'm on the planning page

#### I want to enter iTeach with my existing account

* I go the home page
* I click on Login to enter
* I select my connection mode: Google, OpenID or Username/Password

... using my Google account:

* I select Google
* I validate the authentication with Google if needed
* I'm on the planning page

... using my OpenID account:

* I enter my OpenID and click on the Sign in with OpenID button
* I validate the authentication with the OpenID provider if needed
* I'm on the planning page

... using a custom user name & password:

* I enter my email address & password, and click on _Sign in_
* I'm on the planning page

#### I want to create a school

* I'm on the planning page
* I click on the _New school_ button
* I enter all parameters for the school:
    * name - must be unique for one teacher
    * contact - optional, name of the contact person in this school
    * colour - optional
    * hourly rate - optional, but will be needed for monthly reports and invoices
    * postal address - needed for invoices
    * phone - optional
    * mobile phone - optional
    * email - optional
    * web site - optional

```
GET /api/teacher/school/form
Input: -
Output:
 * 200 UIFormDefinition - list of fields
 * 403 Not authorized

POST /api/teacher/school
Input: UIForm
Output:
 * 201 UISchool
 * 400 Bad format
 * 403 Not authorized
 * 409 School name already existing
```

#### I want to see the parameters of a school

* I'm on the planning page
* I click on the school I want to see
* I'm on the school page and I see all its parameters
    * the school parameters: name, colour and all other filled in parameters
    * the list of its students (enabled & disabled), with the taught matter and the number of hours for this student
    * the total amount of taught hours for the school
    * the list of comments for this school

```
GET /api/teacher/school/<schoolId>
Input: -
Output:
 * 201 UISchool - contains links to the other resources
 * 403 Not authorized
 * 404 Not found
```

#### I want to modify the parameters for a school

#### I want to delete a school

#### I want to add comments about a school

#### I want to update a comment about a school

#### I want to delete a comment about a school

#### I want to create a student

* I'm on the planning page
* I click on the _New student_ button
* I enter all the parameters for the student:
    * name - required, unique in a school
    * school it is associated with - required
    * subject
    * postal address
    * phone
    * mobile phone
    * email
    * web site

```
GET /api/teacher/student/form
Input: -
Output:
 * 200 UIFormDefinition - list of fields
 * 403 Not authorized

POST /api/teacher/student
Input: UIForm
Output:
 * 201 UIStudent
 * 400 Bad format
 * 403 Not authorized
 * 409 Student name already existing in the school
```

#### I want to see the parameters of a student

#### I want to modify the parameters of a student

#### I want to delete a student

#### I want to disable a student

* I'm on the planning page
* I click on the student I want to disable
* On the student page, I click on the _Disable_ button
* I confirm the disabling
* I remain on the student page, with him being now disabled

```
PUT /api/teacher/student/<studentId>
Input: UIForm
Output:
 * 201 UIStudent
 * 400 Bad format
 * 403 Not authorized
 * 409 Student name already existing in the school
```

#### I want to re-enable a student

* I'm on the planning page
* I click on the school the student was belonging to
* On the school page, I click on the student I want to enable
* On the student page, I click on the _Enable_ button
* I remain on the student page, with him being now enabled

```
PUT /api/teacher/student/<studentId>
Input: UIForm
Output:
 * 201 UIStudent
 * 400 Bad format
 * 403 Not authorized
 * 409 Student name already existing in the school
```

#### I want to add comments about a student

#### I want to update a comment about a student

#### I want to delete a comment about a student

#### I want to create a lesson

* I'm on the planning page
* I navigate in the calendar to the period of time where the lesson must be created
* I select an hour range in the chosen day
* I enter all the parameters for the lesson
    * the student - required
    * the location
    * the day - required
    * the time range - required
* after validation, the lesson is displayed in the calendar

```
GET /api/teacher/lesson/form
Input: -
Output:
 * 200 UIFormDefinition - list of fields
 * 403 Not authorized

POST /api/teacher/lesson
Input: UIForm
Output:
 * 201 UILesson
 * 400 Bad format
 * 403 Not authorized
```

#### I want to see the parameters of a lesson

#### I want to modify the parameters of a lesson

#### I want to delete a lesson

#### I want to add comments about a lesson

* I'm on the planning page
* I navigate to the lesson I want to add a comment about
* I click on the lesson
* On the lesson page, I click on the _Add a comment_ button
* I enter the comment and can access a preview for it
* After validation, I'm on the lesson page and the comment is displayed

```
POST /api/teacher/lesson/<lessonId>/comment
Input: UICommentForm
Output:
 * 201 UIComment
 * 400 Bad format
 * 403 Not authorized
```

#### I want to update a comment about a lesson

* I'm on the planning page
* I navigate to the lesson
* I click on the lesson
* On the lesson page, I click on the _Update_ button for the comment I want to edit
* I enter the comment and can access a preview for it
* After validation, I'm on the lesson page and the comment is displayed with its new version

```
PUT /api/teacher/lesson/<lessonId>/comment/<commentId>
Input: UICommentForm
Output:
 * 201 UIComment
 * 400 Bad format
 * 403 Not authorized
 * 404 The comment does not exist
```

#### I want to delete a comment about a lesson

* I'm on the planning page
* I navigate to the lesson
* I click on the lesson
* On the lesson page, I click on the _Delete_ button for the comment I want to delete
* I confirm the deletion
* After confirmation, I'm on the lesson page and the comment has been deleted

```
DELETE /api/teacher/lesson/<lessonId>/comment/<commentId>
Input: UICommentForm
Output:
 * 204 Deleted
 * 403 Not authorized
 * 404 The comment does not exist
```

#### I want to see all the lessons for a student for a given period

* I go the student page (see above).
* I navigate to the period (month per month)

```
GET /api/teacher/lesson
Input: UILessonFilter (student & period)
Output:
 * 200 UILessons
 * 403 Not authorized
 * 404 Student not found
```

#### I want to see all the students for a school

#### I want to see all the lessons for a given period

* I'm on the planning page
* I navigate to the period

```
GET /api/teacher/lesson
Input: UILessonFilter (period)
Output:
 * 200 UILessons
 * 403 Not authorized
```

#### I want to see all the income generated by all the lessons for a given period

* I'm on the planning page
* I click on the _Monthly report_
* I navigate to the month
* I see the income report for this month

```
GET /api/teacher/report
Input: UIReportForm (month)
Output:
 * 200 UIReport (tree per school, student, with a grand total)
 * 400 Bad format
 * 403 Not authorized
```

#### I want to generate an invoice for a school for a given period

##### Invoice set-up

The invoice functionality is available from different locations:

* the school page
* the monthly report, at school level
* the user menu
* the invoice management page

It displays a modal box that allows the user to select:

* the school
* the month and the year for the invoice
* the invoice number
* an optional comment for the invoice
* if the details for the students must be displayed

The school and the month may be already selected, according to the place where the invoice was requested from.

The invoice number is a free numeric field, but its proposed value is computed according to the following rules:

1. the last invoice number, plus one
1. the last invoice number from the user's profile, plus one
1. one

The comment is some text that will be displayed on the invoice. It is optional. Its last value in saved in the user's preferences.

A checkbox allows the user to select if he wants the student details appear on the invoice. This selection is saved in the user's preferences.

##### Invoice creation

When the user validates the invoice creation, the invoice is created and saved as a PDF. The user is notified on the dialog box when the invoice is ready and he can download it.

He can also close the dialog box and go to the _Invoice management_, accessible from the user menu.

##### Invoice management

This page allows the user to list all its invoices, the ones created and the ones pending. He can:

* download the PDF of an invoice at any time.
* delete invoices

Note that the number of invoices is limited to 12 per user.

##### Implementation notes

When the creation of an invoice is requested, the system:

* checks if the user is still allowed to create new invoices (the maximum number of invoices per user is 12).
* checks if the user's profile is complete enough for the creation of an invoice
* checks if the school's profile is complete enough for the creation of an invoice

The PDF for the invoice is generated asynchronously. The invoice is stored on _iTeach_ systems using the invoice input data only. The PDF itself is stored remotely using Amazon S3.

The configuration of the storage is done by the administrator in the _Settings_ page. As long as this setting is not done, no invoice can be generated.

## Non functional requirements

### Platform

* JDK8
* Servlet 3
* HTML 5

### Frameworks

* MongoDB ?
* Spring Data ?
* Spring MVC
* AngularJS
* Bootstrap 3

### Architecture

* Data access layer
* Services - very thin
* UI as REST API
* GUI as HTML 5 client

Additionally:
* flexible edition forms generated by the UI, understood by the GUI
