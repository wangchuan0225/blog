INSERT INTO `question` (`id`, `question_text`, `create_time`) VALUES (1, "What's your favorite food?", datetime('now'));

INSERT INTO `choice` (`id`, `question_id`, `choice_text`, `votes`) VALUES (1, 1, "Pizza", 0);
INSERT INTO `choice` (`id`, `question_id`, `choice_text`, `votes`) VALUES (2, 1, "Hamburger", 0);
INSERT INTO `choice` (`id`, `question_id`, `choice_text`, `votes`) VALUES (3, 1, "Tacos", 0);
