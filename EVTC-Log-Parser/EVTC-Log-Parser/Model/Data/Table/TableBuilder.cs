using EVTC_Log_Parser.Model.Extensions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace EVTC_Log_Parser.Model
{
    public class TableBuilder
    {

        #region Members
        private int cols;
        private int[] widths;
        private string title;
        private List<string[]> rows = new List<string[]>();
        private StringBuilder table = new StringBuilder();
        #endregion

        #region Constructors
        public TableBuilder(string title, string[] header)
        {
            cols = header.Length;
            this.title = title.SurroundWith(" ");
            AddRow(header);
        }
        #endregion

        #region Public Methods
        public void AddRow(string[] row)
        {
            for (int i = 0; i < row.Length; i++)
            {
                row[i] = row[i].SurroundWith(" ");
            }
            rows.Add(row);
        }

        public void AddSeparator()
        {
            if (rows.Count > 0)
            {
                rows.Add(new string[rows[0].Length]);
            }
        }

        public override string ToString()
        {
            GetWidths();
            BuildTitle();
            BuildHeader();
            BuildBody();
            return table.ToString();
        }
        #endregion

        #region Private Methods
        private string CenterString(string str, int len)
        {
            return str.PadRight(((len - str.Length) / 2) + str.Length).PadLeft(len);
        }

        private string LeftAlignString(string str, int len)
        {
            return str.PadRight(len);
        }

        private string RightAlignString(string str, int len)
        {
            return str.PadLeft(len);
        }

        private void GetWidths()
        {
            widths = new int[cols];
            foreach (string[] row in rows)
            {
                for (int i = 0; i < row.Length; i++)
                {
                    if (row[i] != null) { 
                        widths[i] = Math.Max(widths[i], row[i].Length);
                    }
                }
            }
        }

        private void BuildTitle()
        {
            table.Append('\u250C' + "".PadRight(title.Length, '\u2500') + '\u2510' + Environment.NewLine);
            table.Append('\u2502' + title + '\u2502' + Environment.NewLine);
            table.Append('\u2514' + "".PadRight(title.Length, '\u2500') + '\u2518' + Environment.NewLine);
        }

        private void BuildHeader()
        {
            string[] header = rows.First();
            table.Append('\u250C');
            for (int colNum = 0; colNum < cols; colNum++)
            {
                table.Append("".PadRight(widths[colNum], '\u2500'));
                if (colNum != cols - 1)
                {
                    table.Append('\u252C');
                }
                else
                {
                    table.Append('\u2510' + Environment.NewLine + '\u2502');
                }
            }
            for (int colNum = 0; colNum < cols; colNum++)
            {
                table.Append(CenterString(header[colNum], widths[colNum]) + '\u2502');
            }
            table.Append(Environment.NewLine + '\u251C');
            for (int colNum = 0; colNum < cols; colNum++)
            {
                table.Append("".PadRight(widths[colNum], '\u2500'));
                if (colNum != cols - 1)
                {
                    table.Append('\u253C');
                }
                else
                {
                    table.Append('\u2524' + Environment.NewLine);
                }
            }
        }

        private void BuildBody()
        {
            bool includingFooter = false;
            foreach (string[] row in rows.Skip(1))
            {
                for (int colNum = 0; colNum < cols; colNum++)
                {
                    if (row[colNum] == null)
                    {
                        includingFooter = true;
                        if (colNum == 0)
                        {
                            table.Append('\u251C');
                        }
                        else
                        {
                            table.Append('\u253C');
                        }
                        table.Append("".PadRight(widths[colNum], '\u2500'));
                    }
                    else
                    {
                        string text = row[colNum];
                        if (!includingFooter)
                        {
                            if (double.TryParse(text, out double result))
                            {
                                table.Append('\u2502' + RightAlignString(text, widths[colNum]));
                            }
                            else
                            {
                                table.Append('\u2502' + LeftAlignString(text, widths[colNum]));
                            }
                        }
                        else
                        {
                            table.Append('\u2502' + CenterString(text, widths[colNum]));
                        }
                    }
                }
                table.Append((row[0] == null) ? '\u2524' + Environment.NewLine : '\u2502' + Environment.NewLine);
            }
            table.Append('\u2514');
            for (int colNum = 0; colNum < cols; colNum++)
            {
                table.Append("".PadRight(widths[colNum], '\u2500'));
                if (colNum != cols - 1)
                {
                    table.Append('\u2534');
                }
                else
                {
                    table.Append('\u2518');
                }
            }
        }
        #endregion

    }
}
