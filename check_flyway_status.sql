-- Flywayの履歴テーブルを確認するSQL
-- MySQLに接続して実行してください

USE learning_exam_manager;

-- Flywayの履歴テーブルの内容を確認
SELECT * FROM flyway_schema_history ORDER BY installed_rank;

-- 失敗したマイグレーションを確認
SELECT * FROM flyway_schema_history WHERE success = 0;
