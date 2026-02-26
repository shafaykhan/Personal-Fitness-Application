DROP DATABASE IF EXISTS fitness_app_db;
CREATE DATABASE fitness_app_db;
USE fitness_app_db;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS user;
CREATE TABLE user
(
      id               INT PRIMARY KEY AUTO_INCREMENT,
      name             VARCHAR(100)                NOT NULL,
      username         VARCHAR(50)                 NOT NULL,
      password         VARCHAR(255)                NOT NULL,
      email            VARCHAR(150)                NOT NULL,
      contact_no       VARCHAR(20)                 NOT NULL,
      date_of_birth    DATE                        NOT NULL,
      height           DOUBLE                      NULL,
      height_uom_id    INT                         NULL,
      weight           DOUBLE                      NULL,
      weight_uom_id    INT                         NULL,
      gender           ENUM ('MALE', 'FEMALE')     NOT NULL,
      role             ENUM ('ADMIN', 'USER')      NOT NULL,
      status           ENUM ('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
      created_by       INT                         NULL,
      created_at       TIMESTAMP                   NULL,
      last_modified_by INT                         NULL,
      last_modified_at TIMESTAMP                   NULL,
      CONSTRAINT user_height_uom_fk FOREIGN KEY (height_uom_id) references lookup (id),
      CONSTRAINT user_weight_uom_fk FOREIGN KEY (weight_uom_id) references lookup (id),
      CONSTRAINT user_created_by_fk FOREIGN KEY (created_by) references user (id),
      CONSTRAINT user_last_modified_by_fk FOREIGN KEY (last_modified_by) references user (id),
      CONSTRAINT mst_user_username_uindex UNIQUE (username),
      CONSTRAINT mst_user_email_uindex UNIQUE (email),
      CONSTRAINT mst_user_contact_no_uindex UNIQUE (contact_no)
);

DROP TABLE IF EXISTS token;
CREATE TABLE token
(
      id              INT AUTO_INCREMENT PRIMARY KEY,
      user_id         INT          NOT NULL,
      token           VARCHAR(512) NOT NULL,
      revoked         BIT(1)       NOT NULL DEFAULT FALSE,
      last_updated_on TIMESTAMP    NOT NULL,
      created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
      CONSTRAINT token_user_id_fk FOREIGN KEY (user_id) REFERENCES user (id),
      CONSTRAINT token_token_uindex UNIQUE (token)
);

DROP TABLE IF EXISTS lookup;
create table lookup
(
      id               INT PRIMARY KEY AUTO_INCREMENT,
      group_key        VARCHAR(50)                 NOT NULL,
      value            VARCHAR(50)                 NOT NULL,
      status           ENUM ('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
      record_status    BIT(1)                      NOT NULL DEFAULT TRUE,
      created_by       INT                         NULL,
      created_at       TIMESTAMP                   NULL,
      last_modified_by INT                         NULL,
      last_modified_at TIMESTAMP                   NULL,
      CONSTRAINT lookup_created_by_fk FOREIGN KEY (created_by) references user (id),
      CONSTRAINT lookup_last_modified_by_fk FOREIGN KEY (last_modified_by) references user (id)
);

SET FOREIGN_KEY_CHECKS = 1;

DROP TABLE IF EXISTS workout;
CREATE TABLE workout
(
      id               INT PRIMARY KEY AUTO_INCREMENT,
      user_id          INT                         NOT NULL,
      type_id          INT                         NOT NULL,
      duration_uom_id  INT                         NOT NULL,
      intensity_id     INT                         NOT NULL,
      exercise_name    VARCHAR(100)                NOT NULL,
      duration         INT                         NOT NULL,
      calories_burned  INT                         NULL,
      notes            TEXT                        NULL,
      record_date_time TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
      status           ENUM ('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
      record_status    BIT(1)                      NOT NULL DEFAULT TRUE,
      created_by       INT                         NULL,
      created_at       TIMESTAMP                   NULL,
      last_modified_by INT                         NULL,
      last_modified_at TIMESTAMP                   NULL,
      CONSTRAINT workout_user_id_fk FOREIGN KEY (user_id) references user (id),
      CONSTRAINT workout_type_id_fk FOREIGN KEY (type_id) references lookup (id),
      CONSTRAINT workout_duration_uom_id_fk FOREIGN KEY (duration_uom_id) references lookup (id),
      CONSTRAINT workout_intensity_id_fk FOREIGN KEY (intensity_id) references lookup (id),
      CONSTRAINT workout_created_by_fk FOREIGN KEY (created_by) references user (id),
      CONSTRAINT workout_last_modified_by_fk FOREIGN KEY (last_modified_by) references user (id)
);

DROP TABLE IF EXISTS health_record;
CREATE TABLE health_record
(
      id               INT PRIMARY KEY AUTO_INCREMENT,
      user_id          INT                         NOT NULL,
      weight           DOUBLE                      NULL,
      sleep_hours      DOUBLE                      NULL,
      water_intake     DOUBLE                      NULL,
      bmi              DOUBLE                      NULL,
      heart_rate       INT                         NULL,
      steps            INT                         NULL,
      record_date_time TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
      status           ENUM ('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
      record_status    BIT(1)                      NOT NULL DEFAULT TRUE,
      created_by       INT                         NULL,
      created_at       TIMESTAMP                   NULL,
      last_modified_by INT                         NULL,
      last_modified_at TIMESTAMP                   NULL,
      CONSTRAINT health_record_created_by_fk FOREIGN KEY (created_by) references user (id),
      CONSTRAINT health_record_last_modified_by_fk FOREIGN KEY (last_modified_by) references user (id)
);

DROP TABLE IF EXISTS goal;
CREATE TABLE goal
(
      id               INT PRIMARY KEY AUTO_INCREMENT,
      user_id          INT                                      NOT NULL,
      type_id          INT                                      NOT NULL,
      target_value     DOUBLE                                   NOT NULL,
      current_value    DOUBLE                                   NOT NULL,
      description      TEXT                                     NULL,
      start_date       DATE                                     NOT NULL,
      end_date         DATE                                     NULL,
      record_date_time TIMESTAMP                                NOT NULL DEFAULT CURRENT_TIMESTAMP,
      status           ENUM ('ACTIVE', 'INACTIVE', 'COMPLETED') NOT NULL DEFAULT 'ACTIVE',
      record_status    BIT(1)                                   NOT NULL DEFAULT TRUE,
      created_by       INT                                      NULL,
      created_at       TIMESTAMP                                NULL,
      last_modified_by INT                                      NULL,
      last_modified_at TIMESTAMP                                NULL,
      CONSTRAINT goal_user_id_fk FOREIGN KEY (user_id) references user (id),
      CONSTRAINT goal_type_id_fk FOREIGN KEY (type_id) references lookup (id),
      CONSTRAINT goal_created_by_fk FOREIGN KEY (created_by) references user (id),
      CONSTRAINT goal_last_modified_by_fk FOREIGN KEY (last_modified_by) references user (id)
);

DROP TABLE IF EXISTS diet_record;
CREATE TABLE diet_record
(
      id               INT PRIMARY KEY AUTO_INCREMENT,
      user_id          INT                         NOT NULL,
      meal_type_id     INT                         NOT NULL,
      food_name        VARCHAR(150)                NULL,
      calories         INT                         NULL,
      protein          DOUBLE                      NULL,
      carbs            DOUBLE                      NULL,
      fat              DOUBLE                      NULL,
      record_date_time TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
      status           ENUM ('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
      record_status    BIT(1)                      NOT NULL DEFAULT TRUE,
      created_by       INT                         NULL,
      created_at       TIMESTAMP                   NULL,
      last_modified_by INT                         NULL,
      last_modified_at TIMESTAMP                   NULL,
      CONSTRAINT diet_record_user_id_fk FOREIGN KEY (user_id) references user (id),
      CONSTRAINT diet_record_meal_type_id_fk FOREIGN KEY (meal_type_id) references lookup (id),
      CONSTRAINT diet_record_created_by_fk FOREIGN KEY (created_by) references user (id),
      CONSTRAINT diet_record_last_modified_by_fk FOREIGN KEY (last_modified_by) references user (id)
);

DROP TABLE IF EXISTS user_preference;
CREATE TABLE user_preference
(
      id               INT PRIMARY KEY AUTO_INCREMENT,
      user_id          INT          NOT NULL,
      diet_type_id     INT          NULL,
      intensity_id     INT          NULL,
      goal_type_id     INT          NULL,
      allergies        VARCHAR(255) NULL,
      created_by       INT          NULL,
      created_at       TIMESTAMP    NULL,
      last_modified_by INT          NULL,
      last_modified_at TIMESTAMP    NULL,
      CONSTRAINT user_preference_user_id_fk FOREIGN KEY (user_id) references user (id),
      CONSTRAINT user_preference_diet_type_id_fk FOREIGN KEY (diet_type_id) references lookup (id),
      CONSTRAINT user_preference_intensity_id_fk FOREIGN KEY (intensity_id) references lookup (id),
      CONSTRAINT user_preference_goal_type_id_fk FOREIGN KEY (goal_type_id) references lookup (id),
      CONSTRAINT user_preference_created_by_fk FOREIGN KEY (created_by) references user (id),
      CONSTRAINT user_preference_last_modified_by_fk FOREIGN KEY (last_modified_by) references user (id),
      CONSTRAINT user_preference_user_id_uindex UNIQUE (user_id)
);

DROP TABLE IF EXISTS ai_recommendation;
CREATE TABLE ai_recommendation
(
      id               INT PRIMARY KEY AUTO_INCREMENT,
      user_id          INT        NOT NULL,
      type_id          INT        NOT NULL,
      content          MEDIUMTEXT NOT NULL,
      record_date_time TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
      record_status    BIT(1)     NOT NULL DEFAULT TRUE,
      created_by       INT        NULL,
      created_at       TIMESTAMP  NULL,
      last_modified_by INT        NULL,
      last_modified_at TIMESTAMP  NULL,
      CONSTRAINT ai_recommendation_user_id_fk FOREIGN KEY (user_id) references user (id),
      CONSTRAINT ai_recommendation_type_id_fk FOREIGN KEY (type_id) references lookup (id),
      CONSTRAINT ai_recommendation_created_by_fk FOREIGN KEY (created_by) references user (id),
      CONSTRAINT ai_recommendation_last_modified_by_fk FOREIGN KEY (last_modified_by) references user (id)
);


