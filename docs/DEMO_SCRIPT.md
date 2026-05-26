# Demo Script

## 1. Preparation

1. Start SQL Server.
2. Confirm database `Academicresults` exists.
3. Run the seed script:
   `src/main/resources/db/Academicresults_seed.sql`
4. Start the app:
   `mvn spring-boot:run`
5. Open:
   `http://localhost:8080`

## 2. Login And Role Redirect

1. Login as `admin` / `admin123`.
2. Confirm redirect to `/admin/dashboard`.
3. Logout.
4. Login as `employee` / `employee123`.
5. Confirm redirect to `/employee/departments`.
6. Logout.
7. Login as `sv001` / `student123`.
8. Confirm redirect to `/student/dashboard`.

## 3. Authorization Demo

1. Login as `sv001`.
2. Navigate manually to `/employee/results`.
3. Confirm the 403 page appears.
4. Navigate back to `/student/dashboard`.

## 4. Catalog CRUD Demo

1. Login as `employee`.
2. Open `/employee/departments`.
3. Add a department code such as `TEST`.
4. Confirm it appears in the department list.
5. Delete it or leave it as demo data.

## 5. Result Calculation Demo

1. Login as `employee`.
2. Open `/employee/results`.
3. Select a student and course section.
4. Enter:
   - attendanceScore = 8
   - midtermScore = 7
   - finalScore = 9
5. Save as `PUBLISHED`.
6. Confirm total score is calculated as:
   `8 * 0.1 + 7 * 0.3 + 9 * 0.6 = 8.30`
7. Confirm grade is `B`, grade point is `3`, and result is PASS.

## 6. Student Result Visibility

1. Login as `sv001`.
2. Open `/student/results`.
3. Confirm only `sv001` published or locked results appear.
4. Confirm no URL parameter allows viewing `sv002` data.

## 7. Transcript Demo

1. Login as `sv001`.
2. Open `/student/transcript`.
3. Confirm results are grouped by semester.
4. Confirm GPA summary is displayed.

## 8. Closing Notes

- Mention that demo passwords are for course submission only.
- Mention SQL Server seed data is included in `src/main/resources/db/Academicresults_seed.sql`.
- Mention e-commerce modules are intentionally not part of this system.
