package com.example.scriptparsertimetest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.yaml.snakeyaml.Yaml;

import com.yaml.parser.YAMLObject;

import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	RadioGroup rg;
	Button bn;
	TextView tv1, tv2, tv3, tv01, tv02, tv03;

	int groupSelect = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rg = (RadioGroup) findViewById(R.id.rg);

		bn = (Button) findViewById(R.id.btnTest);

		tv1 = (TextView) findViewById(R.id.tv);
		tv01 = (TextView) findViewById(R.id.tv01);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv02 = (TextView) findViewById(R.id.tv02);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv03 = (TextView) findViewById(R.id.tv03);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// 选择脚本组
				switch (checkedId) {
				case R.id.script1:
					groupSelect = 1;
					break;
				case R.id.script2:
					groupSelect = 2;
					break;
				case R.id.script3:
					groupSelect = 3;
					break;
				case R.id.script4:
					groupSelect = 4;
					break;
				default:
					break;
				}
			}
		});
		bn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String content1 = "";
				String content2 = "";
				String content3 = "";
				String time = "";
				long startTime = 0;
				long endTime = 0;
				// TODO Auto-generated method stub
				switch (groupSelect) {
				case 1:
					content1 = getRawString(R.raw.scriptxml1);
					content2 = getRawString(R.raw.scriptjson1);
					content3 = getRawString(R.raw.scriptyaml1);
					break;
				case 2:
					content1 = getRawString(R.raw.scriptxml1);
					content2 = getRawString(R.raw.scriptjson1);
					content3 = getRawString(R.raw.scriptyaml1);
					break;
				case 3:
					content1 = getRawString(R.raw.scriptxml1);
					content2 = getRawString(R.raw.scriptjson1);
					content3 = getRawString(R.raw.scriptyaml1);
					break;
				case 4:
					content1 = getRawString(R.raw.scriptxml1);
					content2 = getRawString(R.raw.scriptjson1);
					content3 = getRawString(R.raw.scriptyaml1);
					break;
				default:
					break;
				}
				tv01.setText("长度：" + String.valueOf(content1.length()));
				tv02.setText("长度：" + String.valueOf(content2.length()));
				tv03.setText("长度：" + String.valueOf(content3.length()));
				
				// get yaml parser time
				startTime = System.nanoTime();
				YAMLObject yo=new YAMLObject();
				yo.YAMLParser2(content3);
				
				endTime = System.nanoTime();
				time = "时间：" + String.valueOf(endTime - startTime) + "ns";
				tv3.setText(time);
				
				// get xml parser time
				startTime = System.nanoTime();
				xmlParser(content1);
				endTime = System.nanoTime();
				time = "时间：" + String.valueOf(endTime - startTime) + "ns";
				tv1.setText(time);



				// get json parser time
				startTime = System.nanoTime();
				try {
					JSONParser(content2);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				endTime = System.nanoTime();
				time = "时间：" + String.valueOf(endTime - startTime) + "ns";
				tv2.setText(time);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// XML脚本解析
	public void xmlParser(String xmlScript) {
		int itemNumber = 0;
		String viName;
		String url = "";
		String[] item = new String[64];
		String[] value = new String[64];
		String script;
		InputStream is = new ByteArrayInputStream(xmlScript.getBytes());
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			;
		}
		Document document = null;
		try {
			document = builder.parse(is);
		} catch (SAXException e) {
			;
		} catch (IOException e) {
			;
		}
		Element rootElement = document.getDocumentElement();
		NodeList nodes = rootElement.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node instanceof Element) {
				Element child = (Element) node;
				viName = child.getNodeName();
				itemNumber = 0;
				NodeList snodes = child.getChildNodes();
				for (int k = 0; k < snodes.getLength(); k++) {
					Node snode = snodes.item(k);
					if (snode instanceof Element) {
						Element schild = (Element) snode;
						item[itemNumber] = schild.getNodeName();
						value[itemNumber] = schild.getAttribute("value");

						// 判断是否为扩展控件
						if (item[itemNumber].equals("URL")) {
							url = value[itemNumber];
						}

						script = schild.getTextContent();
						if (script.length() > 10)
							value[itemNumber] = script;
						itemNumber = itemNumber + 1;
					}
				}
				// addVI
			}
		}
	}

	// YAML脚本解析器1
	public void YAMLParser(String YAMLScript) {
		int itemNumber = 0;
		String viName;
		String url = "";
		String[] item = new String[64];
		String[] value = new String[64];
		Yaml yaml = new Yaml();
		HashMap hmVI = (HashMap) yaml.load(YAMLScript);
		Iterator iterVI = hmVI.entrySet().iterator();
		while (iterVI.hasNext()) {
			Entry entryVI = (Entry) iterVI.next();
			viName = entryVI.getKey().toString();
			HashMap hmAttr = (HashMap) hmVI.get(viName);// 获取当前VI的属性HashMap
			Iterator iterAttr = hmAttr.entrySet().iterator();
			itemNumber = 0;
			while (iterAttr.hasNext()) {
				Entry entryAttr = (Entry) iterAttr.next();
				item[itemNumber] = entryAttr.getKey().toString();
				value[itemNumber] = entryAttr.getValue().toString();
				// 判断是否为扩展控件
				if (item[itemNumber].equals("URL")) {
					url = value[itemNumber];
				}
				itemNumber = itemNumber + 1;
			}
			// add vi
		}
	}

	// YAML脚本解析器2
	public void YAMLParser2(String YAMLScript) {
		String[] content = YAMLScript.split("\n");
		int itemNumber = 0;
		String viName = "";
		String url = "";
		String[] item = new String[64];
		String[] value = new String[64];
		String[] lineContent;
		int contentLength=content.length;
		int lineContentLength;
		String buf;
		for (int i = 0; i < contentLength; i++) {
			lineContent = content[i].split(": ");
			lineContentLength=lineContent.length;
			switch (lineContentLength) {
			case 1:
				if (!viName.equals("")) {
					// add vi
				}
				viName = lineContent[0].trim();
				itemNumber = 0;
				break;
			case 2:
				item[itemNumber] = lineContent[0].trim();
				buf = lineContent[1].trim();
				
				if (buf.startsWith("\"")) {
					value[itemNumber] = buf.substring(1, buf.length() - 1);
				} else {
					value[itemNumber] = lineContent[1].trim();
				}
				// 判断是否为扩展控件
				if (item[itemNumber].equals("URL")) {
					url = value[itemNumber];
				}
				itemNumber++;
				break;
			default:
				break;
			}

		}
	}

	// JSON脚本解析器
	public void JSONParser(String JSONScript) throws JSONException {
		int itemNumber = 0;
		String viName;
		String url = "";
		String[] item = new String[64];
		String[] value = new String[64];
		JSONObject joAll = new JSONObject(JSONScript);
		for (Iterator<String> keys = joAll.keys(); keys.hasNext();) {
			viName = keys.next();
			JSONObject joVI = new JSONObject(joAll.get(viName).toString());
			itemNumber = 0;
			for (Iterator<String> keysAttr = joVI.keys(); keysAttr.hasNext();) {
				item[itemNumber] = keysAttr.next();
				value[itemNumber] = joVI.get(item[itemNumber]).toString();
				// 判断是否为扩展控件
				if (item[itemNumber].equals("URL")) {
					url = value[itemNumber];
				}
				itemNumber = itemNumber + 1;
			}
			// add vi
		}
	}

	// 读取raw内容，返回String
	public String getRawString(int id) {
		InputStream is = getResources().openRawResource(id);
		String s = "";
		try {
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			s = new String(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;

	}

}
