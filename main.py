# -*- coding: utf-8 -*-

import os
from py4j.java_gateway import JavaGateway, GatewayParameters

BIRT_PORT = 25333
BIRT_HOST = '127.0.0.1'


class BirtReport(object):

    def __init__(self, rptdesign_name, rptdesign_path):

        self.rptdesign_path = rptdesign_path
        self.rptdesign_name = rptdesign_name

    def get_report_file(self, ext, filename):
        """
        :param ext: str - file extension, i.e. .doc, .xls
        :param filename: str - filename
        :return: str - filename with extension of the resulting report
        """

        # Params passed to the Birt4Py.java report executor:
        #   1st: .rptdesign full path -> String
        #   2nd: report file full path -> String
        #   3rd: report extension -> String

        filepath = self.rptdesign_path + filename + '.' + ext
        my_list = [self.rptdesign_path + self.rptdesign_name, filepath, ext]

        try:
            gateway = JavaGateway(gateway_parameters=GatewayParameters(auto_convert=True,
                                                                       auto_field=True,
                                                                       port=BIRT_PORT,
                                                                       address=BIRT_HOST))
            gateway.entry_point.getStack(my_list)
        except Exception:
            raise

        return filepath


if __name__ == '__main__':
    rptdesign_name = 'jdbc_test.rptdesign'
    rptdesign_path = os.path.dirname(os.path.abspath(__file__)) + '/'

    birt_report = BirtReport(rptdesign_name, rptdesign_path)

    generated_file = birt_report.get_report_file('docx', 'GenerateDocxFile')
    print(generated_file)


