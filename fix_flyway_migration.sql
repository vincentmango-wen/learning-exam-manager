-- Flywayの失敗したマイグレーションを修復するSQL
-- MySQLに接続して実行してください

USE learning_exam_manager;

-- 方法1: 失敗したマイグレーションを履歴テーブルから削除
DELETE FROM flyway_schema_history WHERE version = '2' AND success = 0;

-- 方法2: すべてのデータを削除してリセット（より安全）
-- 注意: この方法はすべてのデータを削除します
-- DELETE FROM exam_results;
-- DELETE FROM exams;
-- DELETE FROM study_items;
-- DELETE FROM study_subjects;
-- DELETE FROM flyway_schema_history WHERE version = '2';
