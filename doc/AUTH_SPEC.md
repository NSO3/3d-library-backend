🔑 JWT認証 API仕様 (最新版: 管理者/開発者専用)
この認証は、あなたの設計方針に基づき、システム管理者や開発者といった特殊なロールを持つユーザー専用です。一般ユーザーは使用しません。

1. ユーザー新規登録 (Registration)

項目,旧仕様,新仕様
URL,/api/auth/register,エンドポイント削除
初期化,API経由で一般ユーザーが登録可能,src/main/resources/data.sql を使用し、起動時にハッシュ化された管理者ユーザーを自動登録。
備考,管理者アカウントの追加は、DB操作または data.sql の修正によってのみ可能です。,

2. ログインとトークン発行 (Login & Token Issuance)

このエンドポイントは、data.sql で登録された管理者アカウントがアクセス認証を受けるために使用されます。

項目,詳細
URL,/api/auth/login
メソッド,POST
用途,ユーザー名とパスワードで認証し、JWTトークンを発行します。
認可,認証不要 (permitAll())

リクエスト (Request Body)

フィールド名,型,必須,説明
username,String,必須,ログインするユーザー名（data.sql に登録されたもの）
password,String,必須,ログインするパスワード（ハッシュ化前の生パスワード）

// リクエストボディの例 (LoginRequest DTO)
{
  "username": "admin_user",
  "password": "secure-admin-password" 
}

レスポンス (Response Body)

フィールド名,型,説明
accessToken,String,認証成功時に発行されるJWT
tokenType,String,"トークンの種類。常に ""Bearer"""

ステータス,詳細
200 OK,認証とトークン発行に成功。
401 Unauthorized,ユーザー名またはパスワードが正しくない場合。

3. 認証済みAPIへのアクセス方法

ログイン後に取得した accessToken を使用して、保護されたエンドポイントへアクセスします

ヘッダー名,値の形式,適用例
Authorization,Bearer <accessToken>,/api/v1/admin/test (管理者専用API)