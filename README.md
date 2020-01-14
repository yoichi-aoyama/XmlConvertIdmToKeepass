# XmlConvertIdmToKeepass

ID ManagerからKeepass移行した際の単発プログラム
account,password以外に移行したい項目があったので作成。主にコメント欄。

## 前提

- Windows向け
  - プログラム中のファイルエンコーディングをutf-8にしてMacで動作確認済み
- 独自の項目などはプログラムいじらないと変換できない

## Keepassへの移行手順

- ID Manager(IDM)からxml形式でファイルをエクスポート
- プロジェクトルートにinput.xml としてxmlファイルを置くか、XmlConvertIdmToKeepassの引数としてファイルパスを渡す
- プロジェクトルートにoutput.xmlが出力される
- output.xmlをKeepassに読み込ませる
  ※Windows版じゃないとxml読み込ませられないかも

## あとがき

- マッピングが大変だった
- ソース汚い
