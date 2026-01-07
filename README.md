# Learning & Exam Manager

学習進捗・試験結果を管理するためのSpring Boot + Java + SQL学習用CRUDアプリケーション

## 📋 概要

このアプリケーションは、個人開発者が自身の学習進捗と試験結果を管理することを目的としたローカル環境向けのWebアプリケーションです。どの科目をどこまで学習し、試験を受けた結果がどうだったかを一元管理し、進捗率や合格率を可視化することで、学習の計画と振り返りを支援します。

## 🛠️ 技術スタック

- **言語**: Java 23
- **フレームワーク**: Spring Boot 4.0.1
- **ビルドツール**: Gradle
- **データベース**: MySQL
- **テンプレートエンジン**: Thymeleaf
- **マイグレーションツール**: Flyway
- **バリデーション**: Spring Validation

## ✨ 主な機能

### 必須機能

- **学習科目の管理**: 科目の登録・更新・削除・一覧表示
- **学習項目の管理**: 科目ごとに学習項目を登録し、ステータス（未着手／進行中／完了）を更新
- **試験の管理**: 科目ごとに試験を登録し、最大点数・合格点などを設定
- **試験結果の登録**: 試験・得点・合否・受験日を登録
- **一覧表示と簡易検索**: 学習項目や試験結果を一覧表示し、科目やステータスで絞り込み
- **ダッシュボード**: 各科目の進捗率と試験結果の合格率を表示

## 📁 プロジェクト構造

```
learning-exam-manager/
├── src/
│   ├── main/
│   │   ├── java/com/example/learning_exam_manager/
│   │   │   ├── controller/      # コントローラー層
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── SubjectController.java
│   │   │   │   ├── StudyItemController.java
│   │   │   │   ├── ExamController.java
│   │   │   │   └── ExamResultController.java
│   │   │   ├── service/         # サービス層
│   │   │   ├── repository/      # リポジトリ層
│   │   │   ├── entity/          # エンティティクラス
│   │   │   ├── dto/             # データ転送オブジェクト
│   │   │   └── form/            # フォームオブジェクト
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── db/migration/    # Flywayマイグレーションファイル
│   │       └── templates/       # Thymeleafテンプレート
│   └── test/
└── build.gradle
```

## 🚀 セットアップ手順

### 前提条件

- Java 23以上
- Gradle 8.x以上
- MySQL 8.0以上

### 1. リポジトリのクローン

```bash
git clone <repository-url>
cd learning-exam-manager
```

### 2. データベースの準備

MySQLにデータベースを作成します：

```sql
CREATE DATABASE learning_exam_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. アプリケーション設定

`src/main/resources/application.properties` を編集して、データベース接続情報を設定してください：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/learning_exam_manager?useSSL=false&serverTimezone=Asia/Tokyo&characterEncoding=utf8
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 4. アプリケーションのビルドと実行

#### Gradle Wrapperを使用する場合（推奨）

```bash
# Windows
gradlew.bat build

# macOS/Linux
./gradlew build
```

#### アプリケーションの起動

```bash
# Windows
gradlew.bat bootRun

# macOS/Linux
./gradlew bootRun
```

アプリケーションは `http://localhost:8080` で起動します。

## 📊 データベース設計

### 主要テーブル

- **study_subjects**: 学習科目
- **study_items**: 学習項目（科目に紐づく）
- **exams**: 試験（科目に紐づく）
- **exam_results**: 試験結果（試験に紐づく）

詳細なスキーマ定義は `src/main/resources/db/migration/V1__Create_tables.sql` を参照してください。

## 🌐 エンドポイント

- `/` - ダッシュボード
- `/subjects` - 科目一覧
- `/subjects/new` - 科目新規登録
- `/subjects/{id}` - 科目詳細
- `/subjects/{id}/edit` - 科目編集
- `/study-items` - 学習項目一覧
- `/exams` - 試験一覧
- `/exam-results` - 試験結果一覧

## 🧪 テスト

```bash
# テストの実行
./gradlew test
```

## 📝 ライセンス

このプロジェクトは個人開発のMVP（Minimum Viable Product）として設計されており、ローカル環境での動作を前提としています。

## 📚 参考資料

プロジェクトの詳細な設計資料は `Documents/` ディレクトリを参照してください：

- `project_overview.md` - プロジェクト概要
- `database_design.md` - データベース設計
- `architecture_design.md` - アーキテクチャ設計
- `implementation_guide_ver2.md` - 実装ガイド

## 🤝 コントリビューション

このプロジェクトは学習目的で作成されています。改善提案やバグ報告は歓迎します。

