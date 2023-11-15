# Fast-to-Work

Fast-to-Work - это Web приложение доски объявлений, где пользователь может найти себе подходящую вакансию.

## Технологии

Для разработки приложения выбраны следующие технологии и средства языка Java: Spring Boot, Hibernate, PostgreSQL, Thymeleaf, Docker.

## Mockups:

#### Страница поиска:

![alt text](https://github.com/SpanDuy/Fast-to-Work/blob/main/Mockup/SearchPage.png)

#### Страница пользователя:

![alt text](https://github.com/SpanDuy/Fast-to-Work/blob/main/Mockup/UserPage.png)

#### Страница информации о вакансии:

![alt text](https://github.com/SpanDuy/Fast-to-Work/blob/main/Mockup/WorkPage.png)

## UML Diagrams:

#### Registration Process:

![alt text](https://github.com/SpanDuy/Fast-to-Work/blob/main/UML%20diagrams/out/Activity%20Diagram%20Sign%20up%20Algorithm/Registration%20Process.png)

#### Class Diagram:

![Class Diagram](https://github.com/SpanDuy/Fast-to-Work/assets/88227989/b1e9aeb4-6b77-468e-a183-8c24e572e080)

#### Component Diagram:

![Component Diagram](https://github.com/SpanDuy/Fast-to-Work/assets/88227989/a16a873a-21e2-4ed2-b699-70583e2eafac)

#### Deployment Diagram:

![Deployment Diagram](https://github.com/SpanDuy/Fast-to-Work/assets/88227989/3c69e39b-c7ba-4e73-bbb9-4b1d995c4d77)

#### Sequence Diagram:

![Sequence Diagram](https://github.com/SpanDuy/Fast-to-Work/assets/88227989/61e84906-5347-419a-9de2-bb16e4bbe783)

#### State Diagram:

![State Diagram](https://github.com/SpanDuy/Fast-to-Work/assets/88227989/47311c58-b8e6-4caf-ad15-4dc2df25b840)

#### Use Case Diagram:

![Use Case Diagram](https://github.com/SpanDuy/Fast-to-Work/assets/88227989/6fa4a5c8-b464-426e-996f-50293967229c)

#### Flow of Events:

https://github.com/SpanDuy/Fast-to-Work/raw/main/UML%20diagrams/flow-of-events.docx

Flow of events for use case ”Sign up”

	1 Guest press button ”Sign up” on the site.
	2 In the field “username” guest writes his username, in the field “email” guest writes his email, in the field “password” his password and user choose the role (Employer or Employee).
	3 After filling fields guest press button “Continue Registration”.
	4 If user is Employer then his continue registration like a Employer (field “company”).
	5 If user is Employee then his continue registration like a Employee (field “name”, “surname”, “middle name”, “birthday”, “city”, “gender” and “git Link”).
  6 After filling fields user press button “Register”.
	7 System pass to his home page.

Flow of events for use case ”Log in”
 
	1 User press button ”Log in” on the site.
	2 In the field “username” guest writes his name and in the field “password” his password.
	3 After filling fields guest press button “Log in”.
	4 System pass to his home page.

Flow of events for use case ”Create resume/vacancy”

	1 Employee/Employer log in or sign up before creating resume/vacancy.
	2 Employee/Employer choose option on the site “Create resume/vacancy”.
	3 Employee/Employer write resume/vacancy title in the intended field.
	4 Employee/Employer write text of the article in the intended field.
	5 Employee/Employer press button “post” on the site.

Flow of events for use case ”Edit resume/vacancy”

	1 Employee/Employer log in or sign up before editing resume/vacancy.
	2 Employee/Employer choose resume/vacancy that he wants to edit.
	3 Employee/Employer choose option on the site “Edit resume/vacancy”.
	4 Employee/Employer edit resume/vacancy.
	5 Employee/Employer press button “Save changes” on the site.

Flow of events for use case ”Delete resume/vacancy”

	1 Employee/Employer log in or sign up before deleting resume/vacancy.
	2 Employee/Employer choose resume/vacancy that he wants to delete.
	3 Employee/Employer press button “Delete resume/vacancy” on the site.

Flow of events for use case ”View resume/vacancy”

	1 Employee/Employer choose “view resume/vacancy”.
	2 Employee/Employer choose subject area from list.
	3 Employee/Employer select one article to read.
