#!/usr/bin/env python
import random
import pandas
import folium
from branca.colormap import LinearColormap
import json
import pycountry
import time

def update_map_dict():
    try:
        geolocations = '/home/cloudera/Desktop/locations/geolocations.txt' #'D:\\Git-Repo\\twitter-streaming\\src\\main\\oozie\\src\\geolocations.txt'
        with open(geolocations, 'r') as geofile:
            content = geofile.read().splitlines()
            for code in content:
                if code == 'null' or code == '':
                    pass
                else:
                    ccode = pycountry.countries.get(alpha_2=code.upper()).alpha_3
                    if ccode not in map_dict:
                        #print("ccode " + ccode + " not present in dictionary. Adding.")
                        map_dict[ccode] = 1
                    else:
                        #print("ccode " + ccode + " present in dictionary. Updating.")
                        map_dict[ccode] = map_dict.get(ccode) + 1
    except:
        pass

    print(map_dict)
    return LinearColormap(['yellow','red'], vmin = min(map_dict.values()), vmax = max(map_dict.values()))

def get_color(feature):
    value = map_dict.get(feature['properties']['A3'])
    if value is None:
        return '#ffffff' # MISSING -> white
    else:
        return color_scale(value)

all_codes = pandas.read_csv('https://raw.githubusercontent.com/lukes/ISO-3166-Countries-with-Regional-Codes/master/all/all.csv')
map_dict = {}

for code in all_codes['alpha-3']:
    map_dict[code] = 1

while True:
    print("Starting iteration")
    color_scale = update_map_dict()
    print("Colors min: " + str(color_scale.vmin) + " Colors max: " + str(color_scale.vmax))
    countries_geo = 'countries-land.json'

    m = folium.Map(
        location = [42.667542, 18.166191],
        tiles = 'Mapbox Bright',
        zoom_start = 2
    )

    m.choropleth(geo_data = countries_geo,
              data = map_dict,
              columns = ['Country', 'Number of Tweets'],
              key_on = 'feature.properties.A3',
              fill_color = 'YlOrRd',
              fill_opacity = 0.7,
              line_opacity = 0.2,
              legend_name = 'Number of Tweets per Country')


    m.save('choropleth.html')
    print("Map updated.")
    time.sleep(15)
